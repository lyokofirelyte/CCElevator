package com.github.lyokofirelyte.CCElevator;

public class CCEP {

	String name;
	public Boolean mode = false;
	public int task = 0;
	public int inc = 0;
	
	public CCEP(String n){
		name = n;
	}
	
	public void setMode(Boolean a){
		mode = a;
	}
	
	public Boolean getMode(){
		return mode;
	}
	
	public void setTask(int a){
		task = a;
	}
	
	public int getTask(){
		return task;
	}
	
	public void setInc(int a){
		inc = a;
	}
	
	public int getInc(){
		return inc;
	}
}
