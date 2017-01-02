package xstampp.model;

public interface IEntryFilter<T> {

  /**
   * checks a model on whether it is accepted by the filter or not
   *  
   * @param model a model/entry of the generic type T 
   * @return true if the model can/should be used false otherwise
   */
  boolean check(T model);
}
