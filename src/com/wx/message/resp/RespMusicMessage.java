package com.wx.message.resp;

/**
 * 音乐消息
 * @author pajk00150
 *
 */
public class RespMusicMessage extends RespBaseMessage {
	// 音乐  
    private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}  
    
}
