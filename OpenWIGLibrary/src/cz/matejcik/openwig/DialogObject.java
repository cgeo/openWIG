/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.matejcik.openwig;

/**
 *
 * @author matejcik
 */
abstract public class DialogObject {
	public Media media;
	public String text;
	public EventTable sender;
	
	public DialogObject (EventTable sender, String text, Media media) {
		this.sender = sender;
		this.text = text;
		this.media = media;
	}
	
	abstract public void doCallback (Object value);
}
