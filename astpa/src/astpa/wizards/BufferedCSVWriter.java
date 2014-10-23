package astpa.wizards;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
* a Writer Class which extends the functionality of the BufferedWriter 
* to enhance the function to write an csv file 
* 
* @author Lukas Balzer
*
*/
public class BufferedCSVWriter extends BufferedWriter{
	private final char seperator;
	
	/**
	 *
	 * @author Lukas Balzer
	 * @param fileWriter the writer Object which shall be buffered 
	 * @param seperator the separator for the csv file
	 */
	public BufferedCSVWriter(Writer fileWriter, char seperator) {
		super(fileWriter);
		this.seperator= seperator;
	}


	/**
	 * this implementation replaces all occurrences of the seperator char with a
	 * space
	 */
	@Override
	public void write(String text) throws IOException {
		super.write(text.replace(this.seperator, ' '));//$NON-NLS-1$
	}
	
	/**
	 *writes a cell to the CSV file which ends with the separator character
	 * @author Lukas Balzer
	 *
	 * @param text	the contents of the cell
	 * @throws IOException if the text can not be written
	 */
	public void writeCell(String text) throws IOException {
		super.write(text.replace(this.seperator, ' ') + this.seperator);//$NON-NLS-1$
	}
	
	/**
	 *writes a cell to the CSV file which ends with the separator character
	 * @author Lukas Balzer
	 *
	 * @param nr	the contents of the cell
	 * @throws IOException if the text can not be written
	 */
	public void writeCell(int nr) throws IOException {
		super.write(nr);
		super.write(this.seperator);
	}
	
	/**
	 *writes a cell to the CSV file which ends with the separator character
	 * @author Lukas Balzer
	 *
	 * @throws IOException if the text can not be written
	 */
	public void writeCell() throws IOException {
		super.write(this.seperator);
	}
}