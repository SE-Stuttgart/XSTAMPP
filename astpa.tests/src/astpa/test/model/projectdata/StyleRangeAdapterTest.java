package astpa.test.model.projectdata;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.projectdata.StyleRangeAdapter;

/**
 * Test class for the style range adapter
 * 
 * @author Fabian Toth
 * 
 */
public class StyleRangeAdapterTest {
	
	/**
	 * Tests the style range adapter
	 * 
	 * @author Fabian Toth
	 * 
	 * @throws Exception if the marshall or unmarshall process fails
	 */
	@Test
	public void testStyleRangeAdapter() throws Exception {
		StyleRangeAdapter adapter = new StyleRangeAdapter();
		StyleRange styleRange = new StyleRange();
		styleRange.start = 0;
		styleRange.length = 5;
		styleRange.fontStyle = SWT.BOLD;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.fontStyle = SWT.ITALIC;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.fontStyle = SWT.BOLD | SWT.ITALIC;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.fontStyle = SWT.NORMAL;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.strikeout = true;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.strikeout = false;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.underline = true;
		styleRange.underlineStyle = SWT.UNDERLINE_DOUBLE;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.underlineStyle = SWT.UNDERLINE_ERROR;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.underlineStyle = SWT.UNDERLINE_LINK;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.underlineStyle = SWT.UNDERLINE_SINGLE;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		styleRange.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
		Assert.assertEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
		// These tests won't work on the continuous integration server because
		// Dsiplay.getDefault() will be null
		// styleRange.background =
		// Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		// Assert.assertEquals(styleRange,
		// adapter.unmarshal(adapter.marshal(styleRange)));
		// styleRange.foreground =
		// Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		// Assert.assertEquals(styleRange,
		// adapter.unmarshal(adapter.marshal(styleRange)));
		// styleRange.font = Display.getCurrent().getSystemFont();
		styleRange.rise = 6;
		Assert.assertNotEquals(styleRange, adapter.unmarshal(adapter.marshal(styleRange)));
	}
}
