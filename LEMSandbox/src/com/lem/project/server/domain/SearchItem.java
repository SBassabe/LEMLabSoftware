package com.lem.project.server.domain;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.listener.StoreCallback;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class SearchItem implements StoreCallback {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
  private String id;

  @Persistent
  private String parentId;
  @Persistent
  private String word;
 
  public SearchItem() {}

  public String getId() {
	  return id;
  }

  public void setId(String id) {
	  this.id = id;
  }

  public String getParentId() {
	  return parentId;
  }

  public void setParentId(String parentId) {
	  this.parentId = parentId;
  }

  public String getWord() {
	  return word;
  }

  public void setWord(String word) {
	  this.word = word;
  }

  @Override
  public void jdoPreStore() {
	  // TODO Auto-generated method stub
	 
  }
   
} // end class
