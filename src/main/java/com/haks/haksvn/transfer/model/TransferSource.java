package com.haks.haksvn.transfer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="transfer_source")
public class TransferSource {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "transfer_source_seq",unique = true, nullable = false)
	private int transferSourceSeq;
	
	@Column(name = "path", nullable = false, length=500)
	private String path;
	
	@Column(name = "revision", nullable = false)
	private long revision;
	
	@ManyToOne
	@JoinColumn(name="transfer_seq")
	private Transfer transfer;
	
	public TransferSource(){
		
	}

	public int getTransferSourceSeq() {
		return transferSourceSeq;
	}

	public void setTransferSourceSeq(int transferSourceSeq) {
		this.transferSourceSeq = transferSourceSeq;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}
	
	
}
