package com.github.kongwu.recorder.plugin.service;

import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.common.model.PacketConstant;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import com.github.kongwu.recorder.common.utils.AvailablePortFinder;
import com.github.kongwu.recorder.plugin.transport.AgentTunnel;
import com.intellij.execution.process.BaseProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import static com.intellij.notification.NotificationType.*;

import static com.github.kongwu.recorder.common.constant.Constants.NOTIFICATION_GROUP;

public class AgentService {

    private Logger logger = Logger.getInstance(AgentService.class);

    private Map<Long, AgentTunnel> processTunnels = new ConcurrentHashMap<>();

    public void trace(PsiMethod psiMethod) {

        Project project = psiMethod.getProject();

        //方法所在的类名称，全类名
        String className = psiMethod.getContainingClass().getQualifiedName();

        long runningProcessId = getRunningProcessId(project);

        NotificationGroup notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP);

        if(runningProcessId < 0){
            notify(project, notificationGroup, "No running JVM instances", WARNING);
            return ;
        }

        AgentTunnel agentTunnel = processTunnels.computeIfAbsent(runningProcessId,k -> {
            //attach之后，agentServer启动
            int port = attachToTargetVM(runningProcessId);
            AgentTunnel createAgentTunnel = new AgentTunnel(port,project);
            createAgentTunnel.initializeAgentClient();
            return createAgentTunnel;
        });

        //发送trace命令
        ResponsePacket responsePacket = agentTunnel.sendCommand(new RequestPacket(PacketConstant.EVENT_TRACE, className));

        if(responsePacket.getState() == PacketConstant.STATE_OK){
            notify(project, notificationGroup, "Trace successful", INFORMATION);
        }else{
            notify(project, notificationGroup, responsePacket.getBody(), ERROR);
        }

        logger.info("agent tunnel send command successful");
    }

    protected void notify(Project project, NotificationGroup notificationGroup, String body, NotificationType type) {
        notificationGroup
                .createNotification(body, type)
                .notify(project);
    }

    private ProcessHandler getRunningProcess(Project project) {
        RunContentManager contentManager = RunContentManager.getInstance(project);
        RunContentDescriptor runContentDescriptor = contentManager.getSelectedContent();
        if(runContentDescriptor == null){
            return null;
        }
        return runContentDescriptor.getProcessHandler();
    }

    private int attachToTargetVM(long processId) {
        try {
            if (processId >= 0) {
                final VirtualMachine attachedVm = VirtualMachine.attach(String.valueOf(processId));
                Optional<String> pluginPath = getAgentPath();

                logger.info("attaching to target vm: " + processId);

                int availablePort = AvailablePortFinder.getNextAvailable();

                try {
                    if (pluginPath.isPresent()) {
                        attachedVm.loadAgent(pluginPath.get(), String.valueOf(availablePort));
                    }
                }catch (AgentLoadException e){
                    String message = e.getMessage();
                    //java10+ 中,load agent成功也是抛AgentLoadException异常,不过message是0
                    if("0".equals(message)){
                        logger.info("Ignore AgentLoadException, because message is '0'");
                    }else{
                        throw e;
                    }
                }


                attachedVm.detach();
                logger.info("attach to target vm: " + processId + " successful!");

                return availablePort;
            }
        } catch (Throwable e) {
            logger.error("attach to target vm failed!", e);
        }


        return -1;
    }

    private long getRunningProcessId(Project project){
        ProcessHandler runningProcess = getRunningProcess(project);
        try {

            if (runningProcess instanceof BaseProcessHandler) {
                Field field = BaseProcessHandler.class.getDeclaredField("myProcess");
                field.setAccessible(true);
                Object process = field.get(runningProcess);

                if (process instanceof Process) {
                    long processId = getProcessID((Process) process);
                    logger.info("running process pid: " + processId);

                    if (processId >= 0) {
                        return processId;
                    }
                }
            }
        }catch (Throwable e){
            logger.error("get running process pid failed!",e);
        }
        return -1;
    }

    private long getProcessID(@NotNull Process p) {
        long result = -1;
        try {
            String name = p.getClass().getName();
            //for unix based operating systems this should work
            //if field is not found, exception is thrown
            Field f = p.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            result = f.getLong(p);
            f.setAccessible(false);
        } catch (Throwable ex) {
            logger.info("retry get processId");
            try {
                //"java.lang.ProcessImpl" on some systems contains 'pid', on others it contains 'handle'
                Field f = p.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handl = f.getLong(p);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE hand = new WinNT.HANDLE();
                hand.setPointer(Pointer.createConstant(handl));
                result = kernel.GetProcessId(hand);
                f.setAccessible(false);
            } catch (Throwable e) {
                logger.error(e);
                result = -1;
            }
        }
        return result;
    }

    private Optional<String> getAgentPath() {
//        return Optional.of("C:\\Users\\jiang\\IdeaProjects\\recorder-idea-plugin\\agent\\build\\libs\\agent.jar");
        return Optional.of("/Users/jiangxin/IdeaProjects/recorder-idea-plugin/agent/build/libs/agent.jar");
//        try {
//            IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId(Constants.PLUGIN_NAME));
//            Path pluginPath = plugin.getPluginPath();
//
//            //todo  后面改成精准路径
//            return Files.walk(pluginPath)
//                    .filter(file -> file.toFile().getName().equals("agent.jar"))
//                    .findAny().flatMap(t -> Optional.of(t.toAbsolutePath().toString()));
//        } catch (IOException e) {
//            return Optional.empty();
//        }
    }

}
