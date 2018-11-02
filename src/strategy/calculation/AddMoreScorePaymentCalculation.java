/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy.calculation;

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
    public long calculatePoint() {
        
        long plusScore = amount/AddMoreScorePaymentCalculation.configOfSystem.getUnitPayment();
        
        double deltaSquare = Math.pow(this.currentTime-this.latestLoginTime,2.0); 
        
        return (long) (this.currentPoint*Math.pow(ReCalculation.configOfSystem.getBaseOfPower(), -1.0*deltaSquare)) + plusScore;
    }
}
