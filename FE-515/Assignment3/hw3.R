

#install.packages("quantmod")

library(quantmod)


#Question-1

#1.1

VIX <- getOptionChain("^VIX" , NULL)


#1.2
LastQuotePrice <- getQuote("^VIX")$Last
LastQuotePrice

#1.3

for(i in 1:length(VIX)){
  VIX[[i]]$calls$Price <- 0.5*(VIX[[i]]$calls$Bid + VIX[[i]]$calls$Ask)
  VIX[[i]]$puts$Price <- 0.5 * (VIX[[i]]$puts$Bid + VIX[[i]]$puts$Bid)
}


# 1.4

for(i in 1:length(VIX)){
  VIX[[i]]$calls$"In-The-Money" <- (VIX[[i]]$calls$Strike < (rep(c(LastQuotePrice), length(VIX[[i]]$calls$Strike))))
}

for(i in 1:length(VIX)){
  VIX[[i]]$puts$"In-The-Money" <-  VIX[[i]]$puts$Strike > (rep(c(LastQuotePrice), length(VIX[[i]]$puts$Strike)))
}



#1.5

for(i in 1:length(VIX)){
  VIX[[i]]$calls <- VIX[[i]]$calls[c("Strike" , "Bid" , "Ask" , "Price" , "In-The-Money")]
  VIX[[i]]$puts <- VIX[[i]]$puts[c("Strike" , "Bid" , "Ask" , "Price" , "In-The-Money")]
}



today <- format(Sys.Date(), "%Y-%m-%d")
Exp <- names(VIX)
Exp <- as.Date(Exp, format = "%b.%d.%Y") # convert to date object
Exp <- format(Exp, "%Y_%m_%d") # convert to chars with certain format


for (i in 1:length(Exp)){
  write.csv(VIX[[i]]$calls, file = paste("VIXdata" , today, "Exp" , Exp[i] , "calls.csv" , sep = ""))
  write.csv(VIX[[i]]$puts, file = paste("VIXdata" , today, "Exp" , Exp[i] , "puts.csv" , sep = ""))
}



# Question 2

#2.1

sample.skewness <- function(x , adjusted){
  
  x.m3.origin <- mean(x^3)# 3rd sample moment about the origin
  x.m2.origin <- mean(x^2)
  x.sm3 <- x.m3.origin /  (x.m2.origin)^(3/2)
 
  n <- length(x)
  
  if(adjusted == TRUE){
    coff <- sqrt(n * (n-1)) / (n-2)
    x.sm3.adj <- coff * x.sm3
    return(x.sm3.adj)
    
  }else{
    return(x.sm3) 
  }
  
}

#2.2

sample.kurtosis <- function(x , adjusted){
  
  x.m4.origin <- mean(x^4)# 4th sample moment about the origin
  x.m2.origin <- mean(x^2)
  x.sm4 <- x.m4.origin /  (x.m2.origin)^(4/2)
  n <- length(x)
  
  if(adjusted == TRUE){
    coff <- (n-1) / ((n-2) * (n-3))
    x.sm4.adj <- coff * ((n+1)*x.sm4 - 3*(n-1)) + 3
    return(x.sm4.adj)
  }else{
    return(x.sm4) 
  }
}


#2.3

getSymbols(Symbols = "SPY" , from = "2012-01-01" , to = "2013-12-31")
SPY <- data.frame(SPY)
SPY <- SPY[nrow(SPY):1,]


head(SPY)
spy.price <- SPY$SPY.Adjusted
spy.log.price <- log(spy.price)
spy.log.return <- diff(spy.log.price)

#2.4

spy.skewness <- sample.skewness(spy.log.return , FALSE)
spy.skewness

spy.skewness.adj <- sample.skewness(spy.log.return , TRUE)
spy.skewness.adj

#2.5

spy.kurtosis <- sample.kurtosis(spy.log.return , FALSE)
spy.kurtosis

spy.kurtosis.adj <- sample.kurtosis(spy.log.return , TRUE)
spy.kurtosis.adj






