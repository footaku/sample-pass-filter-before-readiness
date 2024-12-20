# What is this?
This is an implementation sample of Filter that allows access from localhost only during `REFUSING_TRAFFIC`.  
ReadinessState remains `REFUSING_TRAFFIC` while WarmupRunner is running, and changes to `ACCEPTING_TRAFFIC` as soon as it is finished.  
When ReadinessState becomes `ACCEPTING_TRAFFIC`, LocalhostDenyFilter denies access from localhost.
