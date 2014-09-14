package com.jonathon_vogel.pennapps;

public class PlayerInfo {
	String regID;
	String nickname;
	boolean hunter;
	boolean ready;
	boolean isSelf;
	
	public PlayerInfo(String regID, String nickname, boolean hunter, boolean ready) {
		super();
		this.regID = regID;
		this.nickname = nickname;
		this.hunter = hunter;
		this.ready = ready;
		this.isSelf = regID.equals(MainActivity.gcmRegistrationId);
	}

	public boolean isHunter() {
		return hunter;
	}

	public void setHunter(boolean hunter) {
		this.hunter = hunter;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public String getRegID() {
		return regID;
	}

	public String getNickname() {
		return nickname;
	}

	public boolean isSelf() {
		return isSelf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hunter ? 1231 : 1237);
		result = prime * result + (isSelf ? 1231 : 1237);
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + (ready ? 1231 : 1237);
		result = prime * result + ((regID == null) ? 0 : regID.hashCode());
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
		if (hunter != other.hunter)
			return false;
		if (isSelf != other.isSelf)
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (ready != other.ready)
			return false;
		if (regID == null) {
			if (other.regID != null)
				return false;
		} else if (!regID.equals(other.regID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlayerInfo [regID=" + regID + ", nickname=" + nickname + ", hunter=" + hunter + ", ready=" + ready + ", isSelf=" + isSelf + "]";
	}
}
