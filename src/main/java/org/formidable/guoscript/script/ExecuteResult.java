package org.formidable.guoscript.script;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecuteResult {
    private String scriptLine = null;
    private boolean successful = true;
    private NextAction nextAction = NextAction.NEXT_LINE;

    public ExecuteResult(){}

    public ExecuteResult(boolean successful){
        this.successful = successful;
    }
}
