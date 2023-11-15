# myapp
## ASSUMPTIONS
1. Records updated in both the topic are always in order, i.e ball 0.2 is always published to topic before ball 0.3.
2. Record pertaining to a ball is published on BOTH the topic. There cannot be the case where update related to ball 0.3 is published to one topic and not to other.
3. There can be more than one update for a ball and the latest update is addressed. Therefore, there can be two output print for a single delivery.
4. Updates to the topic are made per match/innings. 

## DESIGN
1. Since events are not very frequent, the events pertaining to a single match can be produced to a single partition and consumed from the same partition. In the code, partition is not taken when producing or consuming the events.
2. The topics are named as "run" which would contain run events and "wicket" which would contain wicket information.
3. A sample producer is written which replicate the match and innings.

## PREREQUISITES
1. KAFKA topics "run" and "wicket" should be created with one partition (as partition specific is not handled)
   

