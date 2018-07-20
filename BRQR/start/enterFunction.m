function f = enterFunction( x )

    n = length(x);
    
    defcov = [11.0 7.0 14.0 15.0 19.0 16.0 17.0];
    defucov = [-22.0 -34.0 -65.0 -19.0 -38.0 -14.0 -21.0];
    attaucov = [4.0 23.0 30.0 10.0 76.0 16.0 64.0];
    attacov = [-81.0 -11.0 -13.0 -96.0 -25.0 -33.0 -91.0];

%     in = getglobal();
%     defcov = in.defcov;
%     defucov = in.defucov;
%     attaucov = in.attaucov;
%     attacov = in.attacov;

    lamda = 0.12;
    g = 0.0;
    h = 0.0;
    
    for i=1:n

        attautility = x( i ) * attacov( i ) + (1 - x( i )) * attaucov( i );
        defutility  = x( i ) * defcov( i )  + (1 - x( i )) * defucov( i );
        g = g + exp ( lamda * attautility ) * defutility;        

    end

    for i=1:n
    
        attautility = x( i ) * attacov( i ) + (1 - x( i )) * attaucov( i );
        h = h + exp ( lamda * attautility );
    end
    
    f = - g / h;

end

