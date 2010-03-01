package gui;

import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import spreadsheet.CycleFound;
import spreadsheet.Spreadsheet;
import tokens.CellToken;

/**
 * Spreadsheet board contains a table of cells in the spreadsheet GUI.
 * @author Son Pham
 * @version 1.0
 */
public class SpreadSheetBoard extends JPanel
{
  //Instance fields
  
  /**
   * The spreadsheet.
   */
  private Spreadsheet my_spreadsheet;
  
  /**
   * The cell array of this spreadsheet. 
   */
  private CellGui[][] my_cell_array;
  
  /**
   * The rows in this spreadsheet.
   */
  private int my_rows;
  
  /**
   * The columns in this spreadsheet.
   */
  private int my_columns;  
  
  //Constructor
  
  /**
   * Construct a spreadsheet board from the input.
   * @param the_spreadsheet The spreadsheet.
   */
  public SpreadSheetBoard(final Spreadsheet the_spreadsheet)
  {
    super(new GridLayout(the_spreadsheet.getNumRows(), the_spreadsheet.getNumColumns()));
    my_spreadsheet = the_spreadsheet;
    my_rows = the_spreadsheet.getNumRows();
    my_columns = the_spreadsheet.getNumColumns();
    my_cell_array = new CellGui[my_rows][my_columns];
    init();
  }
  
  /**
   * Initialize the cell array and add the cells into the panel.
   */
  private void init()
  {
    for (int i = 0; i < my_rows; i++)
    {
      for (int j = 0; j < my_columns; j++)
      {
	my_cell_array[i][j] = new CellGui(new CellToken(i, j));
	my_cell_array[i][j].setText(my_spreadsheet.cellValueToString(my_cell_array[i][j].getCellToken()));
	my_cell_array[i][j].addMouseListener(new CellMouseListener());
	my_cell_array[i][j].addKeyListener(new CellKeyListener());
	my_cell_array[i][j].addFocusListener(new CellFocusListener());
	add(my_cell_array[i][j]);
      }
    }
  }
  
  /**
   * Mouse listener class for a cell GUI.
   * @author Son Pham
   * @version 1.0
   */
  private class CellMouseListener implements MouseListener
  {
    /**
     * Double click to edit the formula of the cell.
     */
    @Override
    public void mouseClicked(MouseEvent the_event) {
      if (the_event.getClickCount() == 2)
      {
        final CellGui cell_gui = (CellGui) the_event.getComponent();
        cell_gui.setText(my_spreadsheet.cellFormulaToString(cell_gui.getCellToken()));
        cell_gui.setCaretPosition(cell_gui.getDocument().getLength());
      }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      // TODO Auto-generated method stub      
    }

    @Override
    public void mouseExited(MouseEvent the_event) {
      // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
      // TODO Auto-generated method stub      
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      // TODO Auto-generated method stub      
    } 
  }
  
  /**
   * Keyboard listener class for a cell GUI 
   * @author Son Pham
   * @version 1.0 
   */
  private class CellKeyListener implements KeyListener
  {
    @Override
    public void keyTyped(KeyEvent the_event) {
      // TODO Auto-generated method stub      
    }

    /**
     * Press enter to commit change to the cell.
     */
    @Override
    public void keyPressed(KeyEvent the_event) {
      // TODO Auto-generated method stub
      if (the_event.getKeyCode() == KeyEvent.VK_ENTER)
      {
	//System.err.println("Entered");
	final CellGui cell_gui = (CellGui) the_event.getComponent();
	try {
		my_spreadsheet.changeCellFormulaAndRecalculate(cell_gui.getCellToken(), cell_gui.getText());
		cell_gui.setText(my_spreadsheet.cellValueToString(cell_gui.getCellToken()));
		System.err.println(my_spreadsheet.cellValueToString(cell_gui.getCellToken()));
	} catch (CycleFound e) {
		
		JOptionPane.showMessageDialog(null,
		    "Cycle Found Please re-enter formula",
		    "Cycle error",
		    JOptionPane.ERROR_MESSAGE);
		my_spreadsheet.revert(cell_gui.getCellToken());



		
	}
	
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      // TODO Auto-generated method stub      
    }    
  }
  
  private class CellFocusListener implements FocusListener
  {

    @Override
    public void focusGained(FocusEvent the_event) {
      // TODO Auto-generated method stub
    }

    /**
     * Show the value of the cell when losing focus.
     */
    @Override
    public void focusLost(FocusEvent the_event) {
      // TODO Auto-generated method stub
      final CellGui cell_gui = (CellGui) the_event.getComponent();
      cell_gui.setText(my_spreadsheet.cellValueToString(cell_gui.getCellToken()));
    }
    
  }
}