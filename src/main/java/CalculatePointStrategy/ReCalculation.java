/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculatePointStrategy;

/**
 *
 * @author root
 */
public class ReCalculation extends PointCalculation {

    public ReCalculation(long currentPoint, long latestLoginTime, long currentTime){
        super(currentPoint, latestLoginTime, currentTime);
    }

    @Override
    public long calculatePoint() {
        double deltaSquare = Math.pow(this.currentTime-this.latestLoginTime,2.0);  
        return (long) (this.currentPoint*Math.pow(ReCalculation.configOfSystem.getBaseOfPower(), -1.0*deltaSquare));
    }
    
}
