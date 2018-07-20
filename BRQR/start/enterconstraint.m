function [ c,ceq ] = enterconstraint( x )

    n = length(x);
    budget = 15.00;
    time = [9.0 3.0 4.0 8.0 7.0 6.0 5.0];

    %     in = getglobal();
    %     time = in.time;
    
    p = 0.0;    
    for i=1:n
        p = p + x( i ) * time( i );
    end
    
    c = p - budget;
    ceq = [];
    
end

