import random


monomCmds = ['exp', 'exp:', 'coef', 'coef:', 'derivative']
polynomCmds = ['addMonom:', 'multiplyByMonom:', 'asDictionary', 'derivative', \
                'eval:', 'add:']
polyStreamCmds = ['addCoefWithExp:', 'block', 'add:', 'substitute:', 'multiplyBy:', \
        'filter:', 'intersectionWithWithinRange:', 'eval:']


def command(cmd):
    f.write(cmd + '.\n')

def printer(cmd):
    f.write('Transcript show: ({}) ; cr.\n'.format(cmd))

def exception(cmd):
    f.write('[' + cmd + '] on: Error do: [:e | Transcript show: (e messageText) ; cr.].\n')


# Monom test
f = open('monom.in', 'w')

for i in range(5):
    command('m := Monom new')
    for i in range(25):
        cmd = random.choice(monomCmds)
        if (cmd == 'exp' or cmd == 'coef'):
            printer('m ' + cmd)
        elif (cmd == 'exp:' or cmd == 'coef:'):
            arg = random.randint(-6, 6)
            if (arg >= 0 or cmd == 'coef:'):
                command('m ' + cmd + ' ' + '(' + str(arg) + ')')
            else:
                exception('m ' + cmd + ' ' + '(' + str(arg) + ')')
            wrongArg = random.uniform(-6, 6)
            exception('m ' + cmd + ' ' + '(' + str(wrongArg) + ')')
        else: # cmd == derivative 
            command('driv := m derivative')
            printer('m exp')
            printer('m coef')

f.close()


# Polynom test
f = open('polynom.in', 'w')

for i in range(5):
    command('p := Polynom new')
    command('m := Monom new')
    for i in range(25):
        cmd = random.choice(polynomCmds)
        if (cmd == 'addMonom:' or cmd == 'multiplyByMonom:'):
            # check exception
            exception('p ' + cmd + ' $h')
            # create a monom
            coef = random.randint(-6, 6)
            exp = random.randint(0, 4)
            command('m coef: {} ; exp: {}'.format(coef, exp))
            command('p ' + cmd + ' m')
            # check new polynom
            printer('p asDictionary')
            # make sure future change on monom don't affect the polynom
            coef = random.randint(-6, 6)
            exp = random.randint(0, 4)
            command('m coef: {} ; exp: {}'.format(coef, exp))
            # check new polynom
            printer('p asDictionary')
        elif (cmd == 'add:'):
            # check exception
            if (cmd == 'add:'):
                exception('p add: 3')
            # create a random polynoms for the argument
            command('q := Polynom new')
            command('m2 := Monom new')
            for i in range(random.randint(0,4)):
                coef = random.randint(-6, 6)
                exp = random.randint(0, 4)
                command('m2 coef: {} ; exp: {}'.format(coef, exp))
                command('q addMonom: m2')
            # check result
            command('res := p add: q')     
            printer('res asDictionary')
            # make sure future change on p and q don't change res
            command('q := Polynom new')
            command('m2 := Monom new')
            for i in range(random.randint(0,4)):
                coef = random.randint(-6, 6)
                exp = random.randint(0, 4)
                command('m2 coef: {} ; exp: {}'.format(coef, exp))
                command('q addMonom: m2')
            printer('res asDictionary')
        elif (cmd == 'derivative'):
            printer('p derivative asDictionary')
        elif (cmd == 'asDictionary'):
            printer('p asDictionary')
        else: # cmd == eval 
            # check exception
            exception('p eval: {}'.format(random.uniform(-6, 6)))
            # check result
            printer('p eval: {}'.format(random.randint(-6, 6)))

f.close()


# PolyStream test
f = open('polystream.in', 'w')

for i in range(5):
    command('p := PolyStream new')
    for i in range(25):
        cmd = random.choice(polyStreamCmds)
        if (cmd == 'addCoefWithExp:'):
            # check exception
            floatNum = random.uniform(-6, 6)
            exception('p addCoef: 6 withExp: {}'.format(floatNum))
            exception('p addCoef: {}  withExp: 3'.format(floatNum))
            # int nums
            coef = random.randint(-6, 6)
            exp = random.randint(-4, 4)
            if (exp < 0):
                exception('p addCoef: {} withExp: {}'.format(coef, exp))
            else:
                command('p addCoef: {} withExp: {}'.format(coef, exp))
                printer('p eval: {}'.format(random.randint(-5, 5)))
        elif (cmd == 'add:'):
            # check exception
            exception('p add: \'hello\'')
            # create an argument polystream
            command('q := PolyStream new')
            for i in range(random.randint(0, 4)):
                coef = random.randint(-6, 6)
                exp = random.randint(0, 4)
                command('q addCoef: {} withExp: {}'.format(coef, exp))
        elif (cmd == 'substitute:' or cmd == 'multiplyBy'):
            # check exception
            exception('p ' + cmd + ' {}'.format(random.uniform(-6, 6)))
            # check result
            arg = random.randint(-6, 6)
            if (cmd == 'substitute:' and arg == 0):
                exception('p substitute: 0')
            else:
                printer('(p ' + cmd + ' {}) eval: {}'\
                        .format(arg, random.randint(-6, 6)))
        elif (cmd == 'filter:'):
            # create a random set
            command('s := Set new')
            for i in range(random.randint(0, 4)):
                command('s add: {}'.format(random.randint(-1, 5)))
            # check result
            printer('(p filter: s) eval: {}'.format(random.randint(-6, 6)))
            # make sure future change on the set won't affect the polystream
            command('s := Set new')
            for i in range(random.randint(0, 4)):
                command('s add: {}'.format(random.randint(-1, 5)))
            printer('p eval: {}'.format(random.randint(-6, 6)))
        else: # eval
            # check exception
            exception('p eval: {}'.format(random.uniform(-6, 6)))
            # check result
            printer('p eval: {}'.format(random.randint(-6, 6)))

f.close()




