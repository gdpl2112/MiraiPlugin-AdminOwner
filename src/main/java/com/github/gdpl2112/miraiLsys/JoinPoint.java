package com.github.gdpl2112.miraiLsys;

import cn.kloping.lsys.Resource;
import cn.kloping.lsys.entitys.InvokeGroup;
import cn.kloping.lsys.entitys.Request;
import cn.kloping.lsys.entitys.Result;
import cn.kloping.lsys.entitys.User;
import cn.kloping.lsys.workers.Methods;
import io.github.kloping.file.FileUtils;
import io.github.kloping.serialize.HMLObject;
import kotlin.jvm.functions.Function2;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.io.File;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static cn.kloping.lsys.workers.Methods.*;

/**
 * @author github-kloping
 * @version 1.0
 * @Date 2022/1/2-16
 */
public class JoinPoint {
    public static final List<String> ILLEGAL_STR = new LinkedList<>();

    public static void run() {
        try {
            before();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        InvokeGroup ig = new InvokeGroup("admin-owner");
        String ts = "我要头衔.*";
        String methodName = "SetTheTitle";
        ig.getInvokes().put(ts.toString(), methodName);
        ig.getInvokesAfter().put(ts.toString(), new String[]{"<At = ?>\n喜提新头衔"
                , "<At = ?>\n权限不足"
                , "<At = ?>\n违规昵称"
        });
        invokes.put(methodName, SET_THE_TITLE);

        Runnable runnable = () -> {
            if (!Resource.conf.getInvokeGroups().containsKey(ig.getId()))
                Resource.conf.getInvokeGroups().put(ig.getId(), ig);
        };
        Resource.loadConfAfter.add(runnable);
        Resource.i1();
    }

    public static void before() throws ClassNotFoundException {
        File cf = new File(Resource.rootPath, "conf/lsys/adminOwner/illegal.hml");
        String hmlStr = FileUtils.getStringFromFile(cf.getAbsolutePath());
        if (hmlStr.trim().isEmpty()) {
            ILLEGAL_STR.add("艹");
            ILLEGAL_STR.add("C");
            FileUtils.putStringInFile(HMLObject.toHMLString(ILLEGAL_STR), cf);
        } else {
            ILLEGAL_STR.clear();
            HMLObject ho = HMLObject.parseObject(hmlStr);
            List<String> list = (List<String>) ho.toJavaObject();
            ILLEGAL_STR.addAll(list);
        }
    }

    private static final Function2<User, Request, Result> SET_THE_TITLE = new Function2<User, Request, Result>() {
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
}
