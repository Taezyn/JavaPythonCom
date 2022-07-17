import sys

def function1(kwargs):
    print("kwargs dict recieved in function1 : ")
    print(kwargs)
    arg1_f1 = kwargs.get('arg1_f1')
    arg2_f1 = float(kwargs.get('arg2_f1'))
    arg3_f1 = bool(kwargs.get('arg3_f1'))
    # Penser que les arguments seront des strings ici ==> transtyper

    ### Faire des choses :
    print('arg1_f1 :\n | - Value : ' + arg1_f1 + '\n | - Type : ' + str(type(arg1_f1)))
    print('arg2_f1 :\n | - Value : ' + str(arg2_f1) + '\n | - Type : ' + str(type(arg2_f1)))
    print('arg3_f1 :\n | - Value : ' + str(arg3_f1) + '\n | - Type : ' + str(type(arg3_f1)))
    ### ---

    exit(0) # C'est ici ce qui sera récupéré par process.WaitFor() en Java


def function2(kwargs):
    arg1_f2 = float(kwargs.get('arg1_f2'))
    arg2_f2 = float(kwargs.get('arg2_f2'))

    ### Faire des choses :
    mult = arg1_f2 * arg2_f2
    div = arg1_f2 / arg2_f2
    print('mult : ' + str(mult))
    print('div : ' + str(div))
    ### ---

    exit(0)

# Ici les fonctions ne sont pas limitees a ce seul script. Tu peux absolument tout faire.

if __name__ == "__main__":
    # Definir le meme dictionnaire en java
    functions = {1: function1,
                 2: function2}

    # Rappel sur la structure de sys.arv :
    # sys.argv[0] = nom_du_script python (invariable)
    # sys.argv[1] = code de la fonction a appeler dans le dictionnaire functions
    # sys.argv[2] = chaine de caractere contenant 'arg1@val1@arg2@val2@arg3@val3'
    #       Le formatage est explique cote java
    function = functions.get(int(sys.argv[1]))

    # Reconstruit le dictionnaire des arguments a partir de sys.argv[2] comme suit :
    # kwargs = {arg1: val1, arg2: val2, arg3: val3}
    kwargs = {}
    index = 0
    javaArgs = sys.argv[2].split('@')
    lenSysArgs = len(javaArgs)
    while index < lenSysArgs:
        kwargs[javaArgs[index]] = javaArgs[index + 1]
        index += 2

    # Appelle la fonction si elle existe
    if function is not None:
        function(kwargs)
    else:
        exit(200)

    # Cadeau : https://pandas.pydata.org/docs/user_guide/10min.html (tres bon tuto)
