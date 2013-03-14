package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class SVNSourceDiff {

	private String diff;
	private String diffToHtml;
	private SVNSource src;
	private SVNSource trg;
	private boolean isNewContent = false;
	
	public SVNSourceDiff(){
		
	}
	
	@Override
	public String toString(){
		return "\n[ SVNSourceDiff ]\n - diff : " + diff;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}
	
	public String getDiffToHtml(){
		return diffToHtml;
	}
	
	public void setDiffToHtml(String diffToHtml){
		this.diffToHtml = diffToHtml;
	}

	public SVNSource getSrc() {
		return src;
	}

	public void setSrc(SVNSource src) {
		this.src = src;
	}

	public SVNSource getTrg() {
		return trg;
	}

	public void setTrg(SVNSource trg) {
		this.trg = trg;
	}

	public boolean getIsNewContent(){
		return isNewContent;
	}

	public void setIsNewContent(boolean isNewContent){
		this.isNewContent = isNewContent;
	}



	public static class Builder{
		
		private SVNSourceDiff svnSourceDiff;
		
		private Builder(SVNSourceDiff svnSourceDiff){
			this.svnSourceDiff = svnSourceDiff;
		}
		
		public static Builder getBuilder(SVNSourceDiff svnSourceDiff){
			return new Builder(svnSourceDiff);
		}
		
		public SVNSourceDiff build(){
			return svnSourceDiff;
		}
		
		public Builder diff(String diff){
			svnSourceDiff.setDiff(diff);
			return this;
		}
		
		public Builder src(SVNSource src){
			svnSourceDiff.setSrc(src);
			return this;
		}
		
		public Builder trg(SVNSource trg){
			svnSourceDiff.setTrg(trg);
			return this;
		}
		
		public Builder isNewContent(boolean isNewContent){
			svnSourceDiff.setIsNewContent(isNewContent);
			return this;
		}
		
	}
}
