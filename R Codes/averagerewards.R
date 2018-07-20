
getAverageRewards <- function(filepath) {
    
    con = file(filepath, "r")
    options(digits=3)
    count = 0
    vPlot = numeric(0.0)
    avg = 0.0
    
    while ( TRUE ) {
        
        line = readLines(con, n = 1)
        if ( length(line) == 0 ) {
            break
        }
        flag= 1
        v = numeric(0.0)
        if( grepl("Episode",line) ) {
            flag= 0
        } else {
            vec = strsplit(line, ',')
            if(length(vec) != 0)
            count = count + 1
            for(x in vec) {
                v <- c(v, as.double(x))
            }
        }
        if(flag){
            avg = avg + mean(v)
        }
        if(count == 99){
            vPlot <- c(vPlot, avg/(count+1))
            count = 0
            avg = 0
        }
    }
    close(con)
    return(vPlot)
}



vPlot1 <- getAverageRewards("/Users/yatinwadhawan/Documents/Security/Research/8. Smart Grid Security 2017/Q Learning Experiment Dataset/Learning without Hacking state/Sarsa Learning/Exp 1/reward.text")
#vPlot2 <- getAverageRewards("/Users/yatinwadhawan/Documents/Security/Research/8. Smart Grid Security 2017/Q Learning Experiment Dataset/Learning without Hacking state/Sarsa Learning/Exp 1/reward.text")
plot(vPlot1, type="o", col="blue", ann=FALSE)
#lines(vPlot2, type="o",col="red", ann=FALSE)
title(xlab="Trials", col.lab=rgb(0,0.5,0))
title(ylab="Average Rewards per Episode", col.lab=rgb(0,0.5,0))