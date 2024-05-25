package fun.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A vertical flow layout arranges components in a directional flow, much like
 * lines of text in a paragraph. VerticalFlow layouts are typically used to
 * arrange buttons in a panel. It arranges buttons vertically until no more
 * buttons fit on the same line. The line alignment is determined by the
 * <code>align</code> property. The possible values are:
 * <ul>
 * <li>{@link #TOP TOP}
 * <li>{@link #BOTTOM BOTTOM}
 * <li>{@link #CENTER CENTER}
 * </ul>
 * A vertical flow layout lets each component assume its natural (preferred)
 * size.
 */
public class VerticalFlowLayout implements LayoutManager {

	/**
	 * This value indicates that each row of components should be top-justified.
	 */
	public static final int TOP = 0;

	/**
	 * This value indicates that each row of components should be centered.
	 */
	public static final int CENTER = 1;

	/**
	 * This value indicates that each row of components should be
	 * bottom-justified.
	 */
	public static final int BOTTOM = 2;

	/**
	 * <code>align</code> is the property that determines how each column
	 * distributes empty space. It can be one of the following values:
	 * <ul>
	 * <li><code>TOP</code>
	 * <li><code>RIGHT</code>
	 * <li><code>BOTTOM</code>
	 * </ul>
	 *
	 * @see #getAlignment()
	 * @see #setAlignment(int)
	 */
	private int align;

	/**
	 * The vertical flow layout manager allows a separation of components with
	 * gaps. The horizontal gap will specify the space between columns and
	 * between the columns and the borders of the <code>Container</code>.
	 *
	 * 
	 * @see #getHgap()
	 * @see #setHgap(int)
	 */
	private int hgap;

	/**
	 * The vertical flow layout manager allows a separation of components with
	 * gaps. The vertical gap will specify the space between components and
	 * between the components and the borders of the <code>Container</code>.
	 *
	 * @see #getVgap()
	 * @see #setVgap(int)
	 */
	private int vgap;

	/**
	 * If true, components will be filled with <code>Container</code>
	 * horizontally.
	 * 
	 * @see #isHfill()
	 * @see #setHfill(boolean)
	 */
	private boolean hfill;

	/**
	 * Constructs a new <code>VerticalFlowLayout</code> with a centered
	 * alignment and a default 5-unit horizontal and vertical gaps and not
	 * filled with <code>Container</code> horizontally.
	 */
	public VerticalFlowLayout() {
		this(CENTER, 5, 5, false);
	}

	/**
	 * Constructs a new <code>FlowLayout</code> with the specified alignment and
	 * a default 5-unit horizontal and vertical gaps and not filled with
	 * <code>Container</code> horizontally. The value of the alignment argument
	 * must be one of <code>FlowLayout.TOP</code>,
	 * <code>FlowLayout.BOTTOM</code>, <code>FlowLayout.CENTER</code>.
	 * 
	 * @param align
	 *            the alignment value
	 */
	public VerticalFlowLayout(int align) {
		this(align, 5, 5, false);
	}

	/**
	 * Constructs a new <code>VerticalFlowLayout</code> with the indicated
	 * alignment and the indicated horizontal and vertical gaps and not filled
	 * with <code>Container</code> horizontally.
	 * <p>
	 * The value of the alignment argument must be one of
	 * <code>FlowLayout.TOP</code>, <code>FlowLayout.BOTTOM</code>,
	 * <code>FlowLayout.CENTER</code>.
	 * 
	 * @param align
	 *            the alignment value
	 * @param hgap
	 *            the horizontal gap between columns and between the columns and
	 *            the borders of the <code>Container</code>
	 * @param vgap
	 *            the vertical gap between components and between the components
	 *            and the borders of the <code>Container</code>
	 */
	public VerticalFlowLayout(int align, int hgap, int vgap) {
		this(align, hgap, vgap, false);
	}

	/**
	 * Constructs a new <code>VerticalFlowLayout</code> with the indicated
	 * alignment and the indicated horizontal and vertical gaps and the
	 * indicated filled with <code>Container</code> horizontally.
	 * <p>
	 * The value of the alignment argument must be one of
	 * <code>FlowLayout.TOP</code>, <code>FlowLayout.BOTTOM</code>,
	 * <code>FlowLayout.CENTER</code>.
	 * 
	 * @param align
	 *            the alignment value
	 * @param hgap
	 *            the horizontal gap between columns and between the columns and
	 *            the borders of the <code>Container</code>
	 * @param vgap
	 *            the vertical gap between components and between the components
	 *            and the borders of the <code>Container</code>
	 * @param hfill
	 *            the horizontal filling of components in the
	 *            <code>Container</code>
	 */
	public VerticalFlowLayout(int align, int hgap, int vgap, boolean hfill) {
		this.align = align;
		this.hgap = hgap;
		this.vgap = vgap;
		this.hfill = hfill;
	}

	/**
	 * Gets the alignment for this layout. Possible values are
	 * <code>FlowLayout.TOP</code>, <code>FlowLayout.BOTTOM</code>,
	 * <code>FlowLayout.CENTER</code>.
	 * 
	 * @return the alignment value for this layout
	 * @see #setAlignment(int)
	 */
	public int getAlignment() {
		return align;
	}

	/**
	 * Sets the alignment for this layout. Possible values are
	 * <ul>
	 * <li><code>FlowLayout.TOP</code>
	 * <li><code>FlowLayout.BOTTOM</code>
	 * <li><code>FlowLayout.CENTER</code>
	 * </ul>
	 * 
	 * @param align
	 *            one of the alignment values shown above
	 * @see #getAlignment()
	 */
	public void setAlignment(int align) {
		this.align = align;
	}

	/**
	 * Gets the horizontal gap between columns and between the columns and the
	 * borders of the <code>Container</code>
	 *
	 * @return the horizontal gap between columns and between the columns and
	 *         the borders of the <code>Container</code>
	 * @see #setHgap(int)
	 */
	public int getHgap() {
		return hgap;
	}

	/**
	 * Sets the horizontal gap between columns and between the columns and the
	 * borders of the <code>Container</code>.
	 *
	 * @param hgap
	 *            the horizontal gap between columns and between the columns and
	 *            the borders of the <code>Container</code>
	 * @see #getHgap()
	 */
	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	/**
	 * Gets the vertical gap between components and between the components and
	 * the borders of the <code>Container</code>.
	 *
	 * @return the vertical gap between components and between the components
	 *         and the borders of the <code>Container</code>
	 * @see #setVgap(int)
	 */
	public int getVgap() {
		return vgap;
	}

	/**
	 * Sets the vertical gap between components and between the components and
	 * the borders of the <code>Container</code>.
	 *
	 * @param vgap
	 *            the vertical gap between components and between the components
	 *            and the borders of the <code>Container</code>
	 * @see #getVgap()
	 */
	public void setVgap(int vgap) {
		this.vgap = vgap;
	}

	/**
	 * Gets the horizontal filling of components in the
	 * <code>Container</code>.The default is false.
	 *
	 * @return the horizontal filling of components in the
	 *         <code>Container</code>
	 * @see #setHfill(boolean)
	 */
	public boolean isHfill() {
		return hfill;
	}

	/**
	 * Sets the horizontal filling of components in the
	 * <code>Container</code>.The default is false.
	 *
	 * @param hfill
	 *            the horizontal filling of components in the
	 *            <code>Container</code>
	 * @see #isHfill()
	 */
	public void setHfill(boolean hfill) {
		this.hfill = hfill;
	}

	/**
	 * Adds the specified component to the layout. Not used by this class.
	 * 
	 * @param name
	 *            the name of the component
	 * @param comp
	 *            the component to be added
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	/**
	 * Removes the specified component from the layout. Not used by this class.
	 * 
	 * @param comp
	 *            the component to remove
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
	}

	/**
	 * Returns the preferred dimensions for this layout given the <i>visible</i>
	 * components in the specified target container.
	 *
	 * @param target
	 *            the container that needs to be layout
	 * @return the preferred dimensions to layout the subcomponents of the
	 *         specified container
	 * @see #minimumLayoutSize(Container)
	 */
	@Override
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension(0, 0);
			int nmembers = target.getComponentCount();
			boolean firstVisibleComponent = true;
			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					Dimension d = m.getPreferredSize();
					dim.width = Math.max(dim.width, d.width);
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
					} else {
						dim.height += vgap;
					}
					dim.height += d.height;
				}
			}
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right + hgap * 2;
			dim.height += insets.top + insets.bottom + vgap * 2;
			return dim;
		}
	}

	/**
	 * Returns the minimum dimensions needed to layout the <i>visible</i>
	 * components contained in the specified target container.
	 * 
	 * @param target
	 *            the container that needs to be layout
	 * @return the minimum dimensions to layout the sub components of the
	 *         specified container
	 * @see #preferredLayoutSize(Container)
	 */
	@Override
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension(0, 0);
			int nmembers = target.getComponentCount();
			boolean firstVisibleComponent = true;
			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					Dimension d = m.getMinimumSize();
					dim.width = Math.max(dim.width, d.width);
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
					} else {
						dim.height += vgap;
					}
					dim.height += d.height;
				}
			}
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right + hgap * 2;
			dim.height += insets.top + insets.bottom + vgap * 2;
			return dim;
		}
	}

	/**
	 * Layout the container. This method lets each <i>visible</i> component take
	 * its preferred size by reshaping the components in the target container in
	 * order to satisfy the alignment of this <code>VerticalFlowLayout</code>
	 * object.
	 *
	 * @param target
	 *            the specified component being layout
	 */
	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int maxwidth = target.getWidth() - (insets.left + insets.right + hgap * 2);
			int maxheight = target.getSize().height - (insets.top + insets.bottom + vgap * 2);
			int nmembers = target.getComponentCount();
			int x = insets.left + hgap, y = 0;
			int colw = 0, start = 0;
			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					Dimension d = m.getPreferredSize();
					if (hfill) {
						d.width = maxwidth;
					}
					m.setSize(d.width, d.height);
					if ((y == 0) || ((y + d.height) <= maxheight)) {
						if (y > 0) {
							y += vgap;
						}
						y += d.height;
						colw = Math.max(colw, d.width);
					} else {
						colw = moveComponents(target, x, insets.top + vgap, colw, maxheight - y, start, i);
						y = d.height;
						x += hgap + colw;
						colw = d.width;
						start = i;
					}
				}
			}
			moveComponents(target, x, insets.top + vgap, colw, maxheight - y, start, nmembers);
		}
	}

	/**
	 * Centers the elements in the specified column, if there is any slack.
	 * 
	 * @param target
	 *            the component which needs to be moved
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param width
	 *            the width dimensions
	 * @param height
	 *            the height dimensions
	 * @param colStart
	 *            the beginning of the row
	 * @param colEnd
	 *            the the ending of the row
	 * @return actual column width
	 */
	private int moveComponents(Container target, int x, int y, int width, int height, int colStart, int colEnd) {
		switch (align) {
		case TOP:
			y += 0;
			break;
		case CENTER:
			y += height / 2;
			break;
		case BOTTOM:
			y += height;
			break;
		}
		for (int i = colStart; i < colEnd; i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				int cx = x + (width - m.getWidth()) / 2;
				m.setLocation(cx, y);
				y += m.getHeight() + vgap;
			}
		}
		return width;
	}

	/**
	 * Returns a string representation of this <code>VerticalFlowLayout</code>
	 * object and its values.
	 * 
	 * @return a string representation of this layout
	 */
	public String toString() {
		String str = "";
		switch (align) {
		case TOP:
			str = ",align=top";
			break;
		case CENTER:
			str = ",align=center";
			break;
		case BOTTOM:
			str = ",align=bottom";
			break;
		}
		return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + ",hfill=" + hfill + str + "]";
	}

}

