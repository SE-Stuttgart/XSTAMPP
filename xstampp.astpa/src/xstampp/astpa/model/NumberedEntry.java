package xstampp.astpa.model;

import java.util.UUID;

public interface NumberedEntry {
  boolean setNumber(int i);

  int getNumber();
  UUID getId();
}