package com.queendev.punishplugin.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class ChatObject {

	private String mensagem;
	private HoverEvent hevent;
	private ClickEvent cevent;
	
	public ChatObject(String mensagem, HoverEvent hevent, ClickEvent cevent) {
		this.setMensagem(mensagem);
		this.setHoverEvent(hevent);
		this.setClickEvent(cevent);
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public HoverEvent getHoverEvent() {
		return hevent;
	}
	public void setHoverEvent(HoverEvent event) {
		this.hevent = event;
	}
	public ClickEvent getClickEvent() {
		return cevent;
	}
	public void setClickEvent(ClickEvent cevent) {
		this.cevent = cevent;
	}
	
	
	
}
