/*
 * Copyright (c) 2004-2022 The MZmine Development Team
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.mzmine.datamodel.DataPoint;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GnpsLibraryEntry(
    // entry specific
    String spectrum_id, String splash, int ms_level, String Ion_Mode, String Adduct,
    double Precursor_MZ, double ExactMass, int Charge,

    @JsonDeserialize(using = SpectrumDeserializer.class) DataPoint[] peaks_json,

    // compound specific
    String Compound_Name, String Compound_Source, String CAS_Number, String Pubmed_ID,
    // structures
    String Smiles, String INCHI, String INCHI_AUX,

    // instrument specific
    String Ion_Source, String Instrument,
    // contacts
    @JsonProperty("Data_Collector") String ionSource,
    @JsonProperty("PI") String principalInvestigator) {

}
