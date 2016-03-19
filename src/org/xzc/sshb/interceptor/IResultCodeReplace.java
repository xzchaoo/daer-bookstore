package org.xzc.sshb.interceptor;

//一个标记接口,被这个标记的Action类
//比如TestAction的add方法如果返回了success那么结果会被换成 addSuccess
public interface IResultCodeReplace {
}
