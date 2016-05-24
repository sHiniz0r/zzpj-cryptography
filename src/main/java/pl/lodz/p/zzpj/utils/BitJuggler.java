package pl.lodz.p.zzpj.utils;

public final class BitJuggler {

	/**
	 * Wykonuje operacje XOR na dwóch podanch tablicach bajtów.
	 * Jeżeli tablice są różnych rozmiarów, wynikiem jest tablica długości
	 * takiej jak tablica wejściowa o większej długości.
	 * @param src1 - pierwsza wejściowa tablica bajtów.
	 * @param src2 - druga wejściowa tablica bajtów.
	 * @return tablica bajtów zawierająca wynik operacji XOR na tablicach wejściowych.
	 */
	public static byte[] xorArrays(byte[] src1, byte[] src2) {
		if (src1 == null || src2 == null)
			throw new IllegalArgumentException("Source array is null");
		
		if (src1.length > src2.length) 
			src2 = extendArraySize(src2, src1.length);
		
		if (src1.length < src2.length) 
			src1 = extendArraySize(src1, src2.length);
		
		byte[] result = new byte[src1.length];
		for (int i = 0; i < src1.length; i++) {
			result[i] = (byte) (src1[i] ^ src2[i]);
		}

		return result;
	}

	/**
	 * Rozszerza rozmiar tablicy powiekszając ją do podanej długości.
	 * Nowe komórki uzupełniane za zerami.
	 * @param array - tablica którą chcemy poszerzyć
	 * @param newLength - długość o jaką chcemy poszerzyć tablicę
	 * @return poszerzona tablica.
	 */
	public static byte[] extendArraySize(byte[] array, int newLength) {
		if (array == null)
			throw new IllegalArgumentException("Source array is null");
		
		byte[] result = new byte[newLength];
		System.arraycopy(array, 0, result, 0, array.length);
		
		return result;
	}
	
	/**
	 * Zwraca wartość bitu na podanej pozycji w całej tablicy bajtów.
	 * Bity są liczone od lewej do prawej od indeksu 0.
	 * @param source - źródło bajtów.
	 * @param position - pozycja pożądanego bitu.
	 * @return wartosc bitu w postaci wartości całkowitej.
	 */
	public static int getBit(byte[] source, int position) {
		if (source == null) 
			throw new IllegalArgumentException("Source array is null");
		
		int bytePosition = position / 8;
		int bitPositionInByte = position % 8;

		byte trackedByte = source[bytePosition];
		int resultBit = trackedByte >> (8 - (bitPositionInByte + 1)) & 0x0001;

		return resultBit;
	}

	/**
	 * Pobiera określone bity z podanej tablicy bajtów.
	 * @param source - źródło bajtów.
	 * @param startPosition - pozycja od której ma zostać rozpoczęte pobieranie (włacznie).
	 * @param bitsNumber - ilosc bitów do pobrania.
	 * @return Tablica bajów zawierajaca pobrane bity. Wynikiem jest podana ilość
	 * bitów od lewej z uzupełnieniem do wielokrotności liczby 8 zerami.
	 */
	public static byte[] getBits(byte[] source, int startPosition, int bitsNumber) {
		if (source == null) 
			throw new IllegalArgumentException("Source array is null");
		
		int bytesNumber = ((bitsNumber - 1) / 8) + 1;
		byte[] result = new byte[bytesNumber];

		for (int i = 0; i < bitsNumber; i++) {
			int bit = getBit(source, startPosition + i);
			setBit(result, i, bit);
		}

		return result;
	}

	/**
	 * Ustawia bit na podanej pozycji na podana wartość. Numer bitu liczony jest
	 * od lewej do prawej zaczynając od 0.
	 * @param source - źródłowa tablica bajtów.
	 * @param position pozycja bitu do ustawienia.
	 * @param value - wartość na ktora chcemy ustawic bit (1 lub 0).
	 */
	public static void setBit(byte[] source, int position, int value) {
		if (source == null)
			throw new IllegalArgumentException("Source array is null");
		
		if (value != 0 && value != 1)
			throw new IllegalArgumentException("value is not 1 or 0");
		
		int bytePosition = (position) / 8;
		byte changedByte = source[bytePosition];

		if (1 == value) {
			changedByte |= 1 << (8 - ((position % 8) + 1));
		} else {
			changedByte &= ~(1 << (8 - ((position % 8) + 1)));
		}

		source[bytePosition] = changedByte;
	}

	/**
	 * Ustawia określone bity przesłanej tablicy na bity przesłane w tablicy pomocniczej.
	 * @param array - tablica bitów w której zmieniamy bity.
	 * @param startPosition - pozycja (wlacznie) od której ma się zacząć ustawianie bitów. Numeracja od 0.
	 * @param source - bity które mają zostać wprowadzone.
	 * @param startPosition2 - pozycja (wlacznie) od której mają być czytane bity. Numeracja od 0.
	 * @param length ilość bitów z tablicy bits które mają zostać użyte.
	 */
	public static void setBits(byte[] array, int startPosition, byte[] source, int startPosition2, int length) {
		if (array == null) 
			throw new IllegalArgumentException("array is null");
		
		for (int i = startPosition, j = startPosition2; i < startPosition + length; i++, j++) {
			setBit(array, i, getBit(source, j));
		}
	}

	/**
	 * Przesowa bity w lewa strone.
	 * 
	 * @param input
	 *            Wejsciowa tablica bajtow.
	 * @param len
	 *            Ilosc bitow do przesuniecia.
	 * @param pas
	 *            Wiekowsc przesuniecia.
	 * @return Tablica bajtow z przesunietymi bitami.
	 */
	public static byte[] rotLeft(byte[] input, int len, int pas) {
		int nrBytes = (len - 1) / 8 + 1;
		byte[] out = new byte[nrBytes];
		for (int i = 0; i < len; i++) {
			int val = getBit(input, (i + pas) % len);
			setBit(out, i, val);
		}
		return out;
	}

	/**
	 * Rozdziela podany ciag bitow na pomniejsze ciagi. Ilosc bitow musi sie dac
	 * podzilic bez reszty przez parametr lenght.
	 * 
	 * @param input
	 *            Wejsciowy ciag bitow.
	 * @param lenght
	 *            Ilosc bitow w kazdym podciagu.
	 * @return Tablica bajtow gdzie kazdy Bajt zawiera jeden podciag.
	 */
	public static byte[] separateBytes(byte[] input, int lenght) {
		int numOfBytes = (8 * input.length - 1) / lenght + 1;
		byte[] out = new byte[numOfBytes];
		for (int i = 0; i < numOfBytes; i++) {
			for (int j = 0; j < lenght; j++) {
				int val = getBit(input, lenght * i + j);
				setBit(out, 8 * i + j, val);
			}
		}
		return out;
	}

	/**
	 * Laczy dwa ciagi bitow w jeden czytany od lewej do prawej.
	 * 
	 * @param first
	 *            Pierwszy ciag.
	 * @param firstLenght
	 *            Ilosc bitow z pierwszego ciagu liczona od pozycji 0.
	 * @param second
	 *            drugi ciag.
	 * @param secondLenght
	 *            Ilosc bitow z drugiego ciagu liczona od pozycji 0.
	 * @return Tablica bajtow zawierajacy polaczony ciag bitow.
	 */
	public static byte[] concatBits(byte[] first, int firstLenght, byte[] second, int secondLenght) {
		int numOfBytes = (firstLenght + secondLenght - 1) / 8 + 1;
		byte[] output = new byte[numOfBytes];
		int j = 0;

		for (int i = 0; i < firstLenght; i++) {
			int val = getBit(first, i);
			setBit(output, j, val);
			j++;
		}
		for (int i = 0; i < secondLenght; i++) {
			int val = getBit(second, i);
			setBit(output, j, val);
			j++;
		}
		return output;
	}

	/**
	 * Zwraca bitową reprezentację tablicy bajtów jako łańcuch znakowy.
	 * @param source - źródłowa tablica bitów.
	 * @return Łancuch znakowy reprezentujący ciąg bitów.
	 */
	public static String toString(byte[] source) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < source.length * 8; i++) {
			if (i % 8 == 0 && i != 0)
				sb.append(" ");
			sb.append(getBit(source, i));
		}

		return sb.toString();
	}

	private BitJuggler() { }
	
}
