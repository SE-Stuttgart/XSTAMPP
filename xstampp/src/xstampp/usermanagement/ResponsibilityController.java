package xstampp.usermanagement;

import java.util.ArrayList;
import java.util.UUID;

public class ResponsibilityController extends ArrayList<Responsibility> {

  /**
   * 
   */
  private static final long serialVersionUID = -8243551413032695246L;

  public ResponsibilityController() {
  }

  public boolean add(UUID userId, UUID entryId) {
    if (!this.contains(new Responsibility(userId, entryId))) {
      remove(new Responsibility(null, entryId));
      return add(new Responsibility(userId, entryId));
    }
    return false;
  }
}
