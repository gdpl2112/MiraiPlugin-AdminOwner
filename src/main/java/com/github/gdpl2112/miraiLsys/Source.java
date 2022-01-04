package com.github.gdpl2112.miraiLsys;

import cn.kloping.lsys.Resource;
import cn.kloping.lsys.entitys.Request;
import cn.kloping.lsys.entitys.Result;
import cn.kloping.lsys.entitys.User;
import io.github.kloping.arr.Class2OMap;
import kotlin.jvm.functions.Function2;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;

import java.util.LinkedList;
import java.util.List;

import static cn.kloping.lsys.workers.Methods.*;

/**
 * @author github-kloping
 * @version 1.0
 * @Date 2022/1/4-12
 */
public class Source {
    public static final List<String> ILLEGAL_STR = new LinkedList<>();
    public static final JoinPoint INSTANCE = new JoinPoint();
    public static final String GID = "admin-owner";
    public static final String ON_QUIT = "onQuit";
    public static final String ON_KICK = "onKick";
    public static final String ON_ACTIVE_JOIN = "onActiveJoin";
    public static final String ON_INVITE_JOIN = "onActiveJoin";
    public static final String ON_MODIFY_NAME_CARD = "onModifyNameCard";
    public static final String ON_LOST_ADMIN = "onLostAdmin";
    public static final String ON_GET_ADMIN = "onGetAdmin";
    public static final String ON_BOT_LOST_ADMIN = "onBotLostAdmin";
    public static final String ON_BOT_GET_ADMIN = "onBotGetAdmin";

    public static final Function2<User, Request, Result> SET_THE_TITLE = new Function2<User, Request, Result>() {
        @Override
        public Result invoke(User user, Request request) {
            if (request.getEvent() instanceof GroupMessageEvent) {
                GroupMessageEvent gme = (GroupMessageEvent) request.getEvent();
                int l = gme.getGroup().get(gme.getBot().getId()).getPermission().getLevel();
                if (l == MemberPermission.OWNER.getLevel()) {
                    String name = request.getStr().substring(request.getOStr().indexOf(".")).trim();
                    for (String s : ILLEGAL_STR) {
                        if (name.contains(s)) {
                            return state2;
                        }
                    }
                    gme.getGroup().get(user.getQq().longValue()).setSpecialTitle(name);
                    return state0;
                } else {
                    return state1;
                }
            }
            return null;
        }
    };
    public static final Function2<User, Request, Result> SET_THE_ADMIN = new Function2<User, Request, Result>() {
        @Override
        public Result invoke(User user, Request request) {
            if (request.getEvent() instanceof GroupMessageEvent) {
                GroupMessageEvent gme = (GroupMessageEvent) request.getEvent();
                int l = gme.getGroup().get(gme.getBot().getId()).getPermission().getLevel();
                if (l == MemberPermission.OWNER.getLevel()) {
                    if (user.getQq().longValue() == Resource.conf.getQq().longValue()) {
                        At at = Class2OMap.create(request.getEvent().getMessage()).get(At.class);
                        if (at == null) {
                            return state2;
                        } else {
                            gme.getGroup().get(at.getTarget()).modifyAdmin(true);
                            return state0;
                        }
                    } else {
                        return state1;
                    }
                } else {
                    return state1;
                }
            }
            return null;
        }
    };
    public static final Function2<User, Request, Result> UN_THE_ADMIN = new Function2<User, Request, Result>() {
        @Override
        public Result invoke(User user, Request request) {
            if (request.getEvent() instanceof GroupMessageEvent) {
                GroupMessageEvent gme = (GroupMessageEvent) request.getEvent();
                int l = gme.getGroup().get(gme.getBot().getId()).getPermission().getLevel();
                if (l == MemberPermission.OWNER.getLevel()) {
                    if (user.getQq().longValue() == Resource.conf.getQq().longValue()) {
                        At at = Class2OMap.create(request.getEvent().getMessage()).get(At.class);
                        if (at == null) {
                            return state2;
                        } else {
                            gme.getGroup().get(at.getTarget()).modifyAdmin(false);
                            return state0;
                        }
                    } else {
                        return state1;
                    }
                } else {
                    return state1;
                }
            }
            return null;
        }
    };

    public static final class NoOpenException extends RuntimeException {
        public NoOpenException() {
            super("未开启该群");
        }

        public static final NoOpenException INSTANCE = new NoOpenException();
    }
}
