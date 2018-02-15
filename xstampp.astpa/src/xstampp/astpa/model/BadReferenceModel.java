package xstampp.astpa.model;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ITableModel;

public final class BadReferenceModel implements ITableModel {

  private static BadReferenceModel instance;
  private UUID id;

  private BadReferenceModel() {
    this.id = UUID.randomUUID();
  }

  public static BadReferenceModel getBadReference() {
    if (instance == null) {
      instance = new BadReferenceModel();
    }
    return instance;
  }

  @Override
  public String getText() {
    return "";
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public boolean setNumber(int i) {
    return false;
  }

  @Override
  public int getNumber() {
    return 0;
  }

  @Override
  public String getTitle() {
    return "";
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String getIdString() {
    return "";
  }

}
