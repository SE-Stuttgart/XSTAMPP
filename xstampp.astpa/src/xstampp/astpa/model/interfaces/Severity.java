package xstampp.astpa.model.interfaces;

import xstampp.astpa.messages.Messages;

public enum Severity {
  S0(0), S1(1), S2(2), S3(3);

  private int severity;

  private Severity(int severity) {
    this.severity = severity;
  }

  @Override
  public String toString() {
    return "S" + severity;
  }
  
  public String getDescription() {
    switch(severity) {
      case 0:
        return Messages.Severity_S0;
      case 1:
        return Messages.Severity_S1;
      case 2:
        return Messages.Severity_S2;
      case 3:
        return Messages.Severity_S3;
    }
    return "";
  }

}