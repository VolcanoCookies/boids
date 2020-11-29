package net.volcano;

import java.awt.event.*;

public class Input implements KeyListener,
		MouseListener,
		MouseWheelListener,
		MouseMotionListener {
	
	private static final Settings settings = Game.settings;
	
	private final Game game;
	
	private int prevX, prevY;
	
	public Input(Game game) {
		this.game = game;
		
		game.addKeyListener(this);
		game.addMouseListener(this);
		game.addMouseWheelListener(this);
		game.addMouseMotionListener(this);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE -> {
				settings.tickPaused = !settings.tickPaused;
			}
			case KeyEvent.VK_ENTER -> {
				if (settings.tickPaused) {
					game.tick();
				}
			}
			case KeyEvent.VK_R -> {
				settings.xOffset = 0;
				settings.yOffset = 0;
				settings.scale = 1.0;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		prevX = e.getX();
		prevY = e.getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		settings.scale -= e.getWheelRotation() * 0.1;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		settings.xOffset += e.getX() - prevX;
		settings.yOffset += e.getY() - prevY;
		
		prevX = e.getX();
		prevY = e.getY();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		prevX = e.getX();
		prevY = e.getY();
	}
	
	public int getPrevX() {
		return prevX;
	}
	
	public int getPrevY() {
		return prevY;
	}
}
