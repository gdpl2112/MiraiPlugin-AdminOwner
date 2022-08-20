package com.github.gdpl2112.miraiLsys;

import cn.kloping.lsys.Resource;
import cn.kloping.lsys.entitys.InvokeGroup;
import cn.kloping.lsys.workers.Methods;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import org.jetbrains.annotations.NotNull;

import static cn.kloping.lsys.workers.Methods.invokes;
import static com.github.gdpl2112.miraiLsys.Source.*;
import static com.github.gdpl2112.miraiLsys.Utils.getPermissionName;

/**
 * @author github-kloping
 * @version 1.0
 * @Date 2022/1/2-16
 */
public class JoinPoint extends SimpleListenerHost {
    public static void run() {
        try {
            before();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        InvokeGroup ig = new InvokeGroup(GID);
        String ts = "我要头衔.*";
        String methodName = "setTheTitle";

        ig.getInvokes().put(ts.toString(), methodName);
        ig.getInvokesAfter().put(ts.toString(), new String[]{"<At = ?>\n喜提新头衔"
                , "<At = ?>\n权限不足"
                , "<At = ?>\n违规昵称"
        });
        invokes.put(methodName, SET_THE_TITLE);

        //=====
        ts = "设置管理.*";
        methodName = "setTheAdmin";
        ig.getInvokes().put(ts.toString(), methodName);
        ig.getInvokesAfter().put(ts.toString(), new String[]{"<At = ?>\n设置成功"
                , "<At = ?>\n权限不足"
                , "<At = ?>\n未发现At"
        });
        invokes.put(methodName, SET_THE_ADMIN);

        //=====
        ts = "取消管理.*";
        methodName = "unTheAdmin";
        ig.getInvokes().put(ts.toString(), methodName);
        ig.getInvokesAfter().put(ts.toString(), new String[]{"<At = ?>\n取消成功"
                , "<At = ?>\n权限不足"
                , "<At = ?>\n未发现At"
        });
        invokes.put(methodName, UN_THE_ADMIN);

        //=====
        ts = "同意申请";
        methodName = "agreeRequest";
        ig.getInvokes().put(ts.toString(), methodName);
        ig.getInvokesAfter().put(ts.toString(), new String[]{"<At = ?>\n已同意申请"
                , "<At = ?>\n权限不足"
                , "<At = ?>\n暂无入群申请"
        });
        invokes.put(methodName, AGREE_REQUEST);

        //=====
        ts = "拒绝申请";
        methodName = "rejectRequest";
        ig.getInvokes().put(ts.toString(), methodName);
        ig.getInvokesAfter().put(ts.toString(), new String[]{"<At = ?>\n已拒绝申请"
                , "<At = ?>\n权限不足"
                , "<At = ?>\n暂无入群申请"
        });
        invokes.put(methodName, REJECT_REQUEST);

        //=====
        ts = "踢.*";
        methodName = "kickOne";
        ig.getInvokes().put(ts.toString(), methodName);
        ig.getInvokesAfter().put(ts.toString(), new String[]{"<At = ?>\n踢出成功"
                , "<At = ?>\n权限不足"
                , "<At = ?>\n未发现At"
        });
        invokes.put(methodName, KICK_ONE);


        ig.getInvokesAfter().put(ON_ACTIVE_JOIN, new String[]{"<At = $1>\n欢迎新人<Face = 311>\n你是第$2位群员哦"});
        ig.getInvokesAfter().put(ON_INVITE_JOIN, new String[]{"<At = $1>\n欢迎新人<Face = 311>\n你是第$2位群员哦\n邀请者:$3"});
        ig.getInvokesAfter().put(ON_QUIT, new String[]{"有个人从小道溜了<Face = 15>\n($1)"});
        ig.getInvokesAfter().put(ON_KICK, new String[]{"有个人被请出了群聊\n下次注意<Face = 218>\n($1)\n操作者:$2"});
        ig.getInvokesAfter().put(ON_MODIFY_NAME_CARD, new String[]{"有个人改了群名片($1\n旧:$2\n新:$3"});
        ig.getInvokesAfter().put(ON_LOST_ADMIN, new String[]{"哈哈\n$1\n从\"$2\"变成了\"$3\""});
        ig.getInvokesAfter().put(ON_GET_ADMIN, new String[]{"嘻嘻\n$1\n从\"$2\"变成了\"$3\""});
        ig.getInvokesAfter().put(ON_BOT_LOST_ADMIN, new String[]{"呜呜\n$1\n从\"$2\"变成了\"$3\""});
        ig.getInvokesAfter().put(ON_REQUEST_JOIN, new String[]{
                "收到入群申请,请管理员或管理者,回复\"同意申请\"或\"拒绝申请\"\n" +
                        "QQ:$1\n" +
                        "QQ昵称:$2\n" +
                        "请求附带消息:$3\n" +
                        "邀请者:$4"
        });

        Runnable runnable = () -> {
            if (!Resource.conf.getInvokeGroups().containsKey(ig.getId())) {
                Resource.conf.getInvokeGroups().put(ig.getId(), ig);
            }
        };
        Resource.loadConfAfter.add(runnable);
        Resource.i1();
        GlobalEventChannel.INSTANCE.registerListenerHost(Source.INSTANCE);
    }

    public static void before() throws ClassNotFoundException {

    }

    public JoinPoint() {
        super();
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Throwable e0 = exception.getCause().getCause();
        if (e0 == NoOpenException.INSTANCE || e0 instanceof NoOpenException || e0 instanceof NullPointerException) {
            //未开启异常
        } else {
            System.err.println("Please check that the local service is correct\n" +
                    "If confirmed\n" +
                    "Please feedback to https://github.com/gdpl2112/MiraiPlugin-AdminOwner/issues");
            exception.printStackTrace();
        }
    }

    private static final class InvokeGroupHolder {
        static final InvokeGroup INVOKE_GROUP = Resource.conf.getInvokeGroups().get(GID);
    }

    public static void isEnable(long gid) throws NoOpenException {
        if (!Resource.conf.getOpens().contains(gid)) {
            throw NoOpenException.INSTANCE;
        }
    }

    @EventHandler
    public void onActiveJoin(MemberJoinEvent.Active event) {
        isEnable(event.getGroupId());
        String arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_ACTIVE_JOIN)[0];
        Methods.execute0(new Object[]{
                        event.getMember().getId()
                        , event.getGroup().getMembers().size()
                        , "无"
                }
                , arg, event.getGroup(), event.getMember().getId());
    }

    @EventHandler
    public void onInviteJoin(MemberJoinEvent.Invite event) {
        isEnable(event.getGroupId());
        String arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_INVITE_JOIN)[0];
        Object[] os = new Object[]{
                event.getMember().getId()
                , event.getGroup().getMembers().size()
                , event.getInvitor().getId()
        };
        Methods.execute0(os, arg, event.getGroup(), event.getMember().getId());
    }

    @EventHandler
    public void onQuit(MemberLeaveEvent.Quit event) {
        isEnable(event.getGroupId());
        String arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_QUIT)[0];
        Methods.execute0(new Object[]{event.getMember().getId(), event.getGroup().getMembers().size()}
                , arg, event.getGroup(), event.getMember().getId());
    }

    @EventHandler
    public void onKick(MemberLeaveEvent.Kick event) {
        isEnable(event.getGroupId());
        String arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_KICK)[0];
        NormalMember nm = event.getOperator();
        if (nm == null) {
            nm = event.getGroup().get(event.getBot().getId());
        }
        Object[] args = {event.getMember().getId(), nm.getId()};
        Methods.execute0(args, arg, event.getGroup(), event.getMember().getId());
    }

    @EventHandler
    public void onModifyCard(MemberCardChangeEvent event) {
        isEnable(event.getGroupId());
        if (event.getNew().trim().isEmpty()) return;
        String arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_MODIFY_NAME_CARD)[0];
        Object[] args = {event.getMember().getId(), event.getOrigin(), event.getNew()};
        Methods.execute0(args, arg, event.getGroup(), event.getMember().getId());
    }

    @EventHandler
    public void onMemberPermissionChange(MemberPermissionChangeEvent event) {
        isEnable(event.getGroupId());
        String arg;
        if (event.getOrigin().getLevel() > event.getNew().getLevel()) {
            arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_LOST_ADMIN)[0];
        } else {
            arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_GET_ADMIN)[0];
        }
        Object[] args = {event.getMember().getId()
                , getPermissionName(event.getOrigin())
                , getPermissionName(event.getNew())};
        Methods.execute0(args, arg, event.getGroup(), event.getMember().getId());
    }

    @EventHandler
    public void onBotGroupPermissionChange(BotGroupPermissionChangeEvent event) {
        isEnable(event.getGroupId());
        String arg;
        if (event.getOrigin().getLevel() > event.getNew().getLevel()) {
            arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_BOT_LOST_ADMIN)[0];
        } else {
            arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_BOT_GET_ADMIN)[0];
        }
        Object[] args = {"我"
                , getPermissionName(event.getOrigin())
                , getPermissionName(event.getNew())};
        Methods.execute0(args, arg, event.getGroup(), event.getBot().getId());
    }

    @EventHandler
    public void onRequestJoin(MemberJoinRequestEvent event) {
        isEnable(event.getGroupId());
        String arg = InvokeGroupHolder.INVOKE_GROUP.getInvokesAfter().get(ON_REQUEST_JOIN)[0];
        Object[] args = {
                event.getFromId(),
                event.getFromNick(),
                event.getMessage(),
                event.getInvitor() == null ? "无" : event.getInvitorId()
        };
        Methods.execute0(args, arg, event.getGroup(), event.getBot().getId());
        if (MEMBER_JOIN_REQUEST_EVENTS.size() == MAX_ES) {
            MEMBER_JOIN_REQUEST_EVENTS.poll();
        }
        MEMBER_JOIN_REQUEST_EVENTS.offer(event);
    }
}
