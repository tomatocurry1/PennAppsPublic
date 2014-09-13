package com.jonathon_vogel.pennapps;

public class PlayerInfo {
	String nickname;
	boolean ready;
	boolean isSelf;
	
	public PlayerInfo(String nickname, boolean ready, boolean isSelf) {
		super();
		this.nickname = nickname;
		this.ready = ready;
		this.isSelf = isSelf;
	}
	
	public PlayerInfo(String nickname, boolean ready) {
		this(nickname, ready, false);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isSelf ? 1231 : 1237);
		result = prime * result
				+ ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + (ready ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerInfo other = (PlayerInfo) obj;
		if (isSelf != other.isSelf)
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (ready != other.ready)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlayerInfo [nickname=" + nickname + ", ready=" + ready
				+ ", isSelf=" + isSelf + "]";
	}
}
