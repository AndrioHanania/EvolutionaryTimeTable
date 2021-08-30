package engine.stopCondition;

import engine.Engine;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCondition implements StopCondition{

    private static boolean m_FirstExecute = true;
    private boolean m_ReturnValue = false;
    private int m_minutes;
    private Timer timerMinute;
    private TimerTask timerTaskMinute;
    private TimerTask timerTaskUpdateSecond;
    private Timer timerUpdateSecond;

    private int secondLeft;

    public TimeCondition(int minutes)
    {
        m_minutes = minutes;
        timerTaskMinute = null;
        timerTaskUpdateSecond = null;
    }

    public int getMinutes(){return  m_minutes;}

    public void setMinutes(int minutes){m_minutes=minutes;}

    @Override
    public boolean execute(Engine.DataEngine dataEngine) {

        if(m_FirstExecute)
        {
            secondLeft=0;
            m_ReturnValue = false;
            timerMinute = new Timer();
            timerUpdateSecond = new Timer();
            timerTaskMinute = new TimerTask() {
                @Override
                public void run() {
                    m_ReturnValue = true;
                    timerUpdateSecond.cancel();
                    timerMinute.cancel();
                    //secondLeft=0;
                }
            };
            timerTaskUpdateSecond = new TimerTask()
            {
                @Override
                public void run()
                {
                    secondLeft += 1;
                }
            };

            timerMinute.schedule(timerTaskMinute, (long) m_minutes * 60 * 1000);
            timerUpdateSecond.schedule(timerTaskUpdateSecond, 0, 1000);
            m_FirstExecute = false;
        }

        if(m_ReturnValue)
        {
            m_FirstExecute = true;
        }

        return m_ReturnValue;
    }

    public int getSecondLeft(){return secondLeft;}
}
