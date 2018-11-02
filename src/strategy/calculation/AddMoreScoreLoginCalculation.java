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
public class AddMoreScoreLoginCalculation extends PointCalculation{
            
    public AddMoreScoreLoginCalculation(long currentPoint, long latestLoginTime, long currentLoginTime){
        super(currentPoint, latestLoginTime, currentLoginTime);
    }
    
    @Override
    public long calculatePoint() {
        
        long plusScore = AddMoreScoreLoginCalculation.configOfSystem.getPointPerLogin();
        
        long differenceTime = this.currentTime-this.latestLoginTime;
        double deltaSquare = Math.pow(-1.0*differenceTime/configOfSystem.getUnitTime(),2);  
        
        return (long) (this.currentPoint*Math.pow(ReCalculation.configOfSystem.getBaseOfPower(), -1.0*deltaSquare)) + plusScore;
    }
    
}
