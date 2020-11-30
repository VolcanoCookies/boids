package net.volcano;

import java.awt.event.*;

public class Input implements KeyListener,
		MouseListener,
		MouseWheelListener,
		MouseMotionListener {
	
	private static final Settings settings = Game.settings;
	
	private final Game game;
	
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
			case KeyEvent.VK_ESCAPE -> {
				System.exit(0);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	private boolean pressed = false;
	
	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		if (settings.tickPaused) {
			if (Game.hoveringCell != null) {
				Game.hoveringCell.setState(!Game.hoveringCell.alive);
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
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
	
	private int prevCellX, prevCellY;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!settings.tickPaused) {
			settings.xOffset += e.getX() - Game.mouseX;
			settings.yOffset += e.getY() - Game.mouseY;
		}
		
		if (settings.tickPaused && pressed) {
			Game.hoveringCell.setState(true);
		}
		
		Game.mouseX = e.getX();
		Game.mouseY = e.getY();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Game.mouseX = e.getX();
		Game.mouseY = e.getY();
	}
}
