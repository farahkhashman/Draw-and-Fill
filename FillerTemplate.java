import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FillerTemplate {
	
	// constants for our window size
	private final int WWIDTH = 700;
	private final int WHEIGHT = 775;
	private final int PHEIGHT = WHEIGHT-75;
	Color c;
	ArrayList<ArrayList<Point>> list3 = new ArrayList<>();
	
	
	// boolean to keep track of whether to fill in the drawn 
	// pixel art or not
	private boolean filling = false;
	
	// a 100x100 array that keeps track whether to draw a given 'pixel' or not
	private boolean[][] pixels = new boolean[100][100];
	
	public FillerTemplate() {
		
		// normal graphics setup 
		JFrame frame = new JFrame();
		frame.setSize(WWIDTH, WHEIGHT);
		JPanel buttons = new JPanel();
		buttons.setBounds(0,0,WWIDTH,75);
		
		// very simple button with listener
		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filling = false;
			}
		});
		buttons.add(drawButton);
		
		
		// another very simple button
		JButton fillButton = new JButton("Fill");
		fillButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filling = true;
			}
		});
		buttons.add(fillButton);
		
		// a button that you can try out after finishing the basics
		JButton undoButton = new JButton("Undo Fill");
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if you finish everything else, try this out.
				try {
					ArrayList<Point> length = list3.get(list3.size()-1);
					for(int i = 0; i<length.size(); i++) {
						pixels[list3.get(list3.size()-1).get(i).y][list3.get(list3.size()-1).get(i).x] = false;
						System.out.println(list3.get(list3.size()-1).get(i).y);
						System.out.println(list3.get(list3.size()-1).get(i).x);
					}
				list3.remove(list3.size()-1);
				}
				catch(IndexOutOfBoundsException exception){}
				frame.getContentPane().repaint();
			}	
		});
	
		buttons.add(undoButton);
		frame.add(buttons);
		
		// defines how to draw the pixels
		JPanel drawPanel = new JPanel() {
			public void paint(Graphics g) {
				
				// white background
				g.setColor(Color.white);
				g.fillRect(0, WHEIGHT-PHEIGHT, WWIDTH, PHEIGHT);
				
				// for now, just draw the pixels black
				g.setColor(Color.BLACK);
				for (int y = 0; y < pixels.length; y++) 
					for (int x = 0; x < pixels[y].length; x++) 
						if(pixels[y][x]) 
							g.fillRect(x*WWIDTH/100, y*PHEIGHT/100, WWIDTH/100, PHEIGHT/100);
			}
		};
		drawPanel.setBounds(0,75,WWIDTH,PHEIGHT);
		frame.add(drawPanel);
		
		// a motion listener - anytime the mouse is dragged,
		// set the pixel at its location to 'true'
		drawPanel.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if (!filling) 
					changePixels(e.getX(),e.getY());
				frame.getContentPane().repaint();
			}
			public void mouseMoved(MouseEvent e) {}
		});
		
		// mouse listener, mostly here to take care of filling
		drawPanel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				if (!filling)
					changePixels(e.getX(),e.getY());
				else 
					fill(e.getX(),e.getY());
				frame.getContentPane().repaint();
			}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocation(200, 0);
		frame.setResizable(false);
	}
	
	// set the 'pixel' at the x,y coordinates to be 'true'
	public void changePixels(int x, int y) {
		if (x >= 0 && y >= 0 && x < WWIDTH && y < PHEIGHT)
			pixels[y/(PHEIGHT/100)][x/(WWIDTH/100)] = true;
	}
	
	// this is your main job. note that you'll want to work with 
	// pixelX and pixelY - not x and y.
	public void fill(int x, int y) {
		int pixelX = x/(WWIDTH/100), pixelY = y/(PHEIGHT/100);
		ArrayList<Point> list1 = new ArrayList<Point>();
		ArrayList<Point> list2 = new ArrayList<Point>();
		Point start = new Point(pixelX, pixelY);
		list1.add(start);
		while(!list1.isEmpty()) {
			start = new Point(list1.get(0).x, list1.get(0).y);
			pixels[start.y][start.x] = true;
			Point up = new Point(start.x, (start.y-1));
			Point down = new Point(start.x, start.y+1);
			Point right = new Point(start.x+1, start.y);
			Point left = new Point(start.x-1, start.y);
			
			if(start.y>1 && pixels[start.y-1][start.x]==false && !list1.contains(up) && !list2.contains(up)) {
				list1.add(up);
				pixels[start.y-1][start.x] = true;
			}
			if(start.y<98 && pixels[start.y+1][start.x]==false && !list1.contains(down) && !list2.contains(down)) {
				list1.add(down);
				pixels[start.y+1][start.x] = true;
			}
			if(start.x<98 && pixels[start.y][start.x+1]==false && !list1.contains(right) && !list2.contains(right)) {
				list1.add(right);
				pixels[start.y][start.x+1] = true;
			}
			if(start.x>1 && pixels[start.y][start.x-1]==false && !list1.contains(left) && !list2.contains(left)) {
				list1.add(left);
				pixels[start.y][start.x-1] = true;
			}
			
			list2.add(start);
			list1.remove(start);
		}
		list3.add(list2);
	}
	
	public static void main(String[] args) {
		new FillerTemplate();
	}
}