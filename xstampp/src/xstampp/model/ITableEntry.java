package xstampp.model;

import java.util.UUID;

public interface ITableEntry extends Comparable<ITableEntry> {
  
  UUID getId();

  String getTitle();

  String getDescription();

  int getNumber();
}
