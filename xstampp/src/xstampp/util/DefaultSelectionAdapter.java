package xstampp.util;

import java.util.function.Consumer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DefaultSelectionAdapter extends SelectionAdapter {

  private Consumer<SelectionEvent> consumer;

  public DefaultSelectionAdapter(Consumer<SelectionEvent> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void widgetSelected(SelectionEvent e) {
    consumer.accept(e);
  }

}
