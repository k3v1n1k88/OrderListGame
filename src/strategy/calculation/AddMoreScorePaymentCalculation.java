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
public class AddMoreScorePaymentCalculation extends PointCalculation{
    
    private long amount;
    
    public AddMoreScorePaymentCalculation(long currentPoint, long latestLoginTime, long currentLoginTime, long amount){
        super(currentPoint, latestLoginTime, currentLoginTime);
        this.amount = amount;
    }
    
    @Override
    public long calculatePoint() throws CalculationException {
        
        if(this.latestLoginTime > this.currentTime){
            throw new CalculationException("Current login time is smaller than lastestLoginTime");
        }
        
        long plusScore = amount/AddMoreScorePaymentCalculation.configOfSystem.getUnitPayment();
        
        long differenceTime = this.currentTime-this.latestLoginTime;
        double deltaSquare = Math.pow(differenceTime/configOfSystem.getUnitTime(),2); 
        
        return (long) (this.currentPoint*Math.pow(AddMoreScorePaymentCalculation.configOfSystem.getBaseOfPower(), -1.0*deltaSquare)) + plusScore;
    }
}
