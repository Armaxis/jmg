package core.generators.multigen;

import java.util.HashMap;
import java.util.Random;

import jm.JMC;
import util.Tools;
import core.data.DataStorage;
import core.data.Motive;
import core.data.PInstrument;
import core.data.PNote;
import core.data.chords.Chord;
import core.data.patterns.CPattern;
import core.data.patterns.Pattern;
import core.data.patterns.SNPattern;
import core.data.tonality.Tonality;
import core.generators.BaseVoiceGenerator;

public class AccompanimentGenerator extends BaseVoiceGenerator{

	private HashMap<Double, Chord> harmony;
	private int octaveSummand;
	private int lastNote;
	private Double[] CHANCES_OF_NOTE_JUMP = new Double[]{0.5, 0.15, 0.15, 0.07, 0.07, 0.06};//TODO: Move somewhere else;
	private int[] SHIFTED_OCTAVE_ELEMENTS = new int[]{0, 1, 2, 3, 4, 5, 6, -5, -4, -3, -2, -1};
	private Pattern randomPatternForAccomp;
	
	public AccompanimentGenerator() {
		//Nothing here yet :)
	}
	
	@Override
	public void setPossiblePatterns(PInstrument instrument)
	{
		possiblePatterns = DataStorage.INSTRUMENTS_ACCOMPANIMENTS.get(instrument);
		if (possiblePatterns.size() == 0)
			possiblePatterns = DataStorage.BASE_ACCOMPANIMENT_PATTERNS; 
		setCurrentRandomPattern();
	}

	public Motive generateMotive(HashMap<Double, Chord> harmony, Tonality currentTonality, int octaveSummand, int lNote) {
		currentMotive = new Motive();
		
		this.currentTonality = currentTonality;
		this.harmony = harmony;
		this.octaveSummand = octaveSummand;
		this.lastNote = lNote;
		
		if (this.lastNote == 0)
			this.lastNote = currentTonality.getPitch();
		
		generateSecondRun();
		convertSecondRunToStables();
		
		generateThirdRun();
		return currentMotive;
	}
	
	public void setCurrentRandomPattern()
	{
		//Выбираем произвольный паттерным которым будет вестись наполнение
		//Он будет жить аж 4 такта
		randomPatternForAccomp = possiblePatterns.get(new Random().nextInt(possiblePatterns.size()));
	}

	private void generateSecondRun()
	{
		//Переводим последнюю ноту в абсолютную величину в диапазоне от -5 до 6
		int absLastNote =lastNote % 12;
		if (absLastNote > 6)
			absLastNote = absLastNote - 12;
		
		//Выбираем первый аккорд в гармонии для текущего такта
		Chord chord = harmony.get(0.0d);
		
		//Основываясь на предыдущей ноте и текущем аккорде произвольным образом выбираем с какой ноты будем начинать аккомпанемент
		//Нота должна принадлежать аккорду, быть в абсолютном виде (0-11) и при этом стоять не далее чем на 6 полутонов от предыдущей ноты
		//Если подходящих нот несколько - между ними бросается жребий согласно вероятностям. Чем больше расстояние до новой ноты - тем меньше шанс что ее выберут
		//Вероятности берутся из CHANCES_OF_NOTE_JUMP
		int firstNoteInAccomp = pickNoteCloseToExisting(chord, absLastNote);
		
		//При наполнении нотами аккомпанемента отталкиваемся от идее, что длина нот в аккомпанементе должна равняться шестнадцатой. 
		//Так как у нас всюду 4/4, то считаем сколько нот у нас в паттерне. Если 4 - до делаем 4 раза по 4. Если 8 - то два раза по 8.
		//Итого итерируемся от 0 до 16 / (кол-во нот в паттерне + 1) (т.к. еще первую ноту добавлять надо к паттерну)
		int maxIterations = 16 / (int)(randomPatternForAccomp.getPatternRelativeLength() + 1);
		for (int i = 0; i < maxIterations; i++)
		{
			CPattern currentPattern = new CPattern();
			
			//Считаем какая это по счету четверть в такте, основываясь на i
			double rhythmValue = i * (4 / maxIterations);
			chord = harmony.get(Tools.nearestKey(harmony, rhythmValue));
			
			boolean chordContainsFirstNote = false;
			for (int k = 0; k < chord.getChord().length; k ++)
			{
				if(chord.getChord()[k] == (firstNoteInAccomp + 12) % 12 )
				{
					chordContainsFirstNote = true;
					break;
				}
			}
			
			if(!chordContainsFirstNote)
			{
				firstNoteInAccomp = pickNoteCloseToExisting(chord, firstNoteInAccomp);
			}
			
			currentPattern.addPattern(
					new SNPattern(
						new PNote(
							firstNoteInAccomp + 60 + octaveSummand, 
							JMC.QUARTER_NOTE
						)
					)
			);
			
			
			currentPattern.addPattern(randomPatternForAccomp);
			
			currentPattern.multiplyFirstNoteLength(0.25);
		
			currentMotive.secondRunPatterns.add(currentPattern);
		}
		
	}
	
	private int pickNoteCloseToExisting(Chord chord, int prevNote)
	{
		//Заводим дополнительный массив для аккорда. Мы должны дополнить аккорд еще и отрицательными значениями - чтобы подойти к предыдущей ноте с двух сторон
		//Если chord = [0,4,7] то мы строим doubledChord как [-12, -8, -5, 0, 4, 7], т.е. первая половина = [chord(i) - 12], а вторая половина неизменный chord  
		int[] doubledChord = new int[chord.getChord().length*2];
		
		int chordLength = chord.getChord().length;
		
		//Заполняем doubledChord нужными значениями
		for (int k = 0; k < chordLength; k++)
		{
			doubledChord[ 		k		] 	= chord.getChord()[k] - 12;
			doubledChord[k + chordLength] 	= chord.getChord()[k];
		}
		
		
		//Нота не должна отстоять слишком далеко от основного тона тональности, т.е. если основная нота си, то должно все быть в диапазоне -5..6 от этой ноты
		//Необходимо перевести высоту тона в диапазон -5..6 чтобы при расчете нот doubleChords правильно брать расстояние
		int pitchValue = SHIFTED_OCTAVE_ELEMENTS[currentTonality.getPitch() % 12];
		
		//Заводим мап в который будем записывать кандидатов в стартовые ноты и их вероятности
		HashMap<Integer, Double> firstNoteCandidates = new HashMap<Integer, Double>();
		for (int k = 0; k < doubledChord.length; k++)
		{
			
			//Note shouldn't be far from main note in tonality
			if (Math.abs(doubledChord[k] - pitchValue) > 6)
				continue;
			
			//Вычисляем расстояние между нотами
			int distanceBetweenNotes = Math.abs(doubledChord[k] - prevNote);
			if (distanceBetweenNotes <= 5) //5 = quart is maximum to jump. 
			{
				//Если расстояние меньше кварты - добавляем его к кандидатам
				firstNoteCandidates.put(doubledChord[k], CHANCES_OF_NOTE_JUMP[distanceBetweenNotes]);
			}
		}
		
		//Вызываем функцию, которая рандомно возьмет кандидата из мапа (с учетом весов) и возвращаем его
		return Tools.pick(firstNoteCandidates);
	}
	
	private void convertSecondRunToStables() 
	{
		for (int i = 0; i < currentMotive.secondRunPatterns.size(); i++)
		{
			//Finally insert this pattern into SecondRunStables
			currentMotive.secondRunStables.add(currentMotive.secondRunPatterns.get(i));
		}
	}


	private void generateThirdRun() {
		for (int i = 0; i < currentMotive.secondRunStables.size(); i++)
			currentMotive.thirdRunPatterns.add(new CPattern((CPattern)currentMotive.secondRunStables.get(i)));	
	}
}
