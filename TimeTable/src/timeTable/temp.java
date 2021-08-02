package timeTable;

public enum temp {

    Rule1(/*false*/)
            {
            //    @Override
                public boolean isValidInSystem(TimeTable timeTable){
                    return true;
                }

            },
    Rule2(/*true*/)
            {
              //  @Override
                public boolean isValidInSystem(TimeTable timeTable){
                    return true;
                }

            };


    /*private boolean m_StatusObligation;

    public void setStatusObligation(boolean statusObligation)
    {
        m_StatusObligation = statusObligation;
    }

    public boolean getStatusObligation()
    {
        return m_StatusObligation;
    }

    Rules(boolean statusObligation)
    {
        m_StatusObligation = statusObligation;
    }*/

    //public abstract boolean isValidInSystem(TimeTable timeTable);
}
