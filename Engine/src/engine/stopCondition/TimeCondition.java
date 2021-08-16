package engine.stopCondition;

import engine.Engine;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCondition implements StopCondition{

    private static boolean m_FirstExecute = true;
    private boolean m_ReturnValue = false;
    private int m_Second;
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            m_ReturnValue = true;
        }
    };

    public TimeCondition(int second)
    {
        m_Second = second;
    }

    @Override
    public boolean execute(Engine.DataEngine dataEngine) {

        if(m_FirstExecute)
        {
            timer.schedule(timerTask, m_Second * 1000);
            m_FirstExecute = false;
        }

        if(m_ReturnValue)
        {
            m_FirstExecute = true;
        }

        return m_ReturnValue;
    }
}
