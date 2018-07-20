function [x,fval,exitflag,output,lambda,grad,hessian] = start()
    %% This is an auto generated MATLAB file from Optimization Tool.
    import start.*;
    rng(0,'twister');

    %% Start with the default options
    options = optimoptions('fmincon');
    %% Modify options setting
    %%options = optimoptions(options,'Display', 'iter-detailed');
    options = optimoptions(options,'Algorithm', 'interior-point');
    options = optimoptions(options,'PlotFcn', 'optimplotfval');
    options = optimoptions(options,'TolX',1e-10);
    
    n = 20;
    s = 7;
    opt = Inf;
    optx = [];
    
%     in = Data(s);
%     setglobal(in);
    
    for j=1:n

        a = 0;
        b = 1;
        x0 = [];
        lb = [];
        ub = [];
        %%Genrating Random Values for coverage vector.
        for i=1:s    
           lb( i ) = 0;
           ub( i ) = 1;
           x0(i) = (b-a).*rand(1,1) + a;
        end

%         fprintf ( 1, ' x1= %10f\n', x0 );
        
        [x,fval,exitflag,output,lambda,grad,hessian] = ...
        fmincon(@enterFunction,x0,[],[],[],[],lb,ub,@enterconstraint,options);

        fprintf ( 1, '\n Function Value: %12e\n', fval );
%         fprintf ( 1, '\n Coverage:   %12e', x);

        if opt > fval
            opt = fval;
            optx = x;
        end
    end
    
    fprintf ( 1, '\n Optimal Function Value: %12e\n', opt );
    fprintf ( 1, '\n Optimal Coverage Probabilities:   %12e\n', optx);
    
    defcov = [11.0 7.0 14.0 15.0 19.0 16.0 17.0];
    defucov = [-22.0 -34.0 -65.0 -19.0 -38.0 -14.0 -21.0];

    val = 0.0;
    for i=1:length(optx)
        val = val + x( i ) * defcov( i )  + (1 - x( i )) * defucov( i );
    end
    fprintf ( 1, '\n Defender Expected Value: %12e\n', val );
end