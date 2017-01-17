package xstpa.ui;

import xstampp.astpa.ui.causalScenarios.CausalScenariosView;

public class BasicScenariosTable extends CausalScenariosView {
  public BasicScenariosTable() {
    super(true, false, true);
    columns[1] = "Basic Scenarios";
  }
}
