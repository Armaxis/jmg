/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {
	public GBC(int gridx, int gridy, int gridwidth, int gridheight, double weightX, double weightY, int constraintX, int constraintY) {
        super(gridx, gridy, gridwidth, gridheight, weightX, weightY, constraintX, constraintY, new Insets(1, 1, 1, 1),0,0);
	}
     public GBC(int gridx, int gridy, int gridwidth, int gridheight, double weightX, double weightY) {
          super(gridx, gridy, gridwidth, gridheight, weightX, weightY, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0);
     }
     public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
         super(gridx, gridy, gridwidth, gridheight, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0);
     }
     public GBC fill(int fill) {
         this.fill = fill;
         return this;
     }
     public GBC anchor(int anchor) {
    	 this.anchor = anchor;
         return this; 
     }

}
