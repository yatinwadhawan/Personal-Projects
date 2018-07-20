
avergeRewards = function(filepath) {
    
    con = file(filepath, "r")
    options(digits=3)
    count = 0
    vPlot = numeric(0.0)
    
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
        if(flag)
            vPlot <- c(vPlot, mean(v))
    }
    print(vPlot)
    plot(vPlot, type="o", col="blue", ann=FALSE)
    title(xlab="Trials", col.lab=rgb(0,0.5,0))
    title(ylab="Average Rewards per Episode", col.lab=rgb(0,0.5,0))
    close(con)
}

avergeRewards("/Users/yatinwadhawan/Documents/Results/reward.text")
