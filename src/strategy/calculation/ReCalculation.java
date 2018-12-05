/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy.calculation;

import exception.CalculationException;

/**
 *
 * @author root
 */
public class ReCalculation extends PointCalculation {

    public ReCalculation(long currentPoint, long latestLoginTime, long currentTime){
        super(currentPoint, latestLoginTime, currentTime);
    }

    @Override
    public long calculatePoint() throws CalculationException {
        if(this.latestLoginTime > this.currentTime){
            throw new CalculationException("Current login time is smaller than lastestLoginTime"
                    + "\n" + "current time: "+this.currentTime
                    + "\n" + "latest login time"+this.latestLoginTime);
        }
        
        double deltaSquare = Math.pow(this.currentTime-this.latestLoginTime,2.0);  
        return (long) (this.currentPoint*Math.pow(ReCalculation.configOfSystem.getBaseOfPower(), -1.0*deltaSquare));
    }
    
}
