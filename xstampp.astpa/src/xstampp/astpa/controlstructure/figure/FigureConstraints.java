package xstampp.astpa.controlstructure.figure;

public enum FigureConstraints {

  /**
   * A constant meaning that there is no constraint at all on the component
   */
  NONE,

  /**
   * A constant meaning that the with of the component is determined by the width of it's children,
   * if there are no children a default width must be defined for the component.
   */
  WIDTH,

  /**
   * A constant meaning that the height of the component is determined by the width of it's
   * children, if there are no children a default width must be defined for the component.
   */
  HEIGHT,

  /**
   * A layout constraint that implicates that children of a component with this style should be layout in a Grid
   * structure meaning that they can be moved so that they fit in the grid.
   */
  GRID_CONSTRAINT
}
