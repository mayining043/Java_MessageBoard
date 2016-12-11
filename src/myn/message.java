package myn;

import java.text.*;
import java.util.*;

public class message {
	protected  String id;
	protected  String from;
	protected  String to;
	protected  String msg;
	protected  String kind;
	protected  String time;
	/**
	 * 构造函数
	 * @param f
	 * @param t
	 * @param m
	 * @param k
	 */
	public message(String id,String f,String t,String m,String k,String time){
		this.id=id;
		from=f;
		to=t;
		msg=m;
		kind=k;
		this.time=time;
	}
	public message(String f,String t,String m,String k){
		id=null;
		from=f;
		to=t;
		msg=m;
		kind=k;
		time=new String(DateFormat.getDateTimeInstance().format(
				new Date()
				));
	}
	@Override
	public String  toString(){
		String t=null;
		t="#"+this.id+" "+this.getTime()+" From:"+this.getFrom()+" To:"+this.getTo()
			+"\n"+this.getMsg()+"(Kind："+this.getKind()+")";
		return t;
	}
	/**
	 * id
	 * @param in
	 */
	public void setId(String in){
		this.id=in;
	}
	public String getId(){
		return this.id;
	}
	/**
	 * 发信人
	 * @param in
	 */
	public void setFrom(String in){
		this.from=in;
	}
	public String getFrom(){
		return this.from;
	}
	/**
	 * 收信人
	 * @param in
	 */
	public void setTo(String in){
		this.to=in;
	}
	public String getTo(){
		return this.to;
	}
	/**
	 * 内容
	 * @param in
	 */
	public void setMsg(String in){
		this.msg=in;
	}
	public String getMsg(){
		return this.msg;
	}
	/**
	 * 种类
	 * @param in
	 */
	public void setKind(String in){
		this.kind=in;
	}
	public String getKind(){
		return this.kind;
	}
	/**
	 * 时间
	 * @param in
	 */
	public void setTime(Date in){
		this.time=new String(DateFormat.getDateTimeInstance().format(in));;
	}
	public String getTime(){
		return this.time;
	}
}
