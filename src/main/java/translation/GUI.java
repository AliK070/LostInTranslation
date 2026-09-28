package translation;

import javax.swing.*;
import java.awt.event.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Translator translator = new JSONTranslator();
            CountryCodeConverter countryConverter = new CountryCodeConverter();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();

            String[] countries = new String[translator.getCountryCodes().size()];

            for(int i = 0; i < translator.getCountryCodes().size(); i++){
                String countryCode = translator.getCountryCodes().get(i);
                countries[i] = countryConverter.fromCountryCode(countryCode);
            }

            JList<String> countryList = new JList<>(countries);

            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            countryList.setVisibleRowCount(8);
            countryList.setFixedCellWidth(150);

            JScrollPane countryScroll = new JScrollPane(countryList);
            JPanel countryPanel = new JPanel();

            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countryScroll);

            String[] languages = new String[translator.getLanguageCodes().size()];

            for(int i = 0; i < translator.getLanguageCodes().size(); i++){
                String languageCode = translator.getLanguageCodes().get(i);
                languages[i] = languageConverter.fromLanguageCode(languageCode);
            }

            JComboBox<String> languageDropDown = new JComboBox<>(languages);
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageDropDown);

            JPanel resultPanel = new JPanel();
            resultPanel.add(new JLabel("Translation:"));
            JLabel resultLabel = new JLabel(" ");
            resultPanel.add(resultLabel);

            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    updateTranslation(
                            countryList,
                            languageDropDown,
                            resultLabel,
                            translator,
                            countryConverter,
                            languageConverter
                    );
                }
            });

            languageDropDown.addActionListener(e -> {
                updateTranslation(
                        countryList,
                        languageDropDown,
                        resultLabel,
                        translator,
                        countryConverter,
                        languageConverter
                );
            });

            JPanel mainPanel = new JPanel();

            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);

            JFrame frame = new JFrame("Country Name Translator");

            frame.setContentPane(mainPanel);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }


    private static void updateTranslation(
            JList<String> countryList,
            JComboBox<String> languageBox,
            JLabel resultLabel,
            Translator translator,
            CountryCodeConverter countryConverter,
            LanguageCodeConverter languageConverter) {

        String country = countryList.getSelectedValue();

        String language = (String) languageBox.getSelectedItem();

        if (country == null || language == null) {
            return;
        }

        String countryCode = countryConverter.fromCountry(country);
        String languageCode = languageConverter.fromLanguage(language);

        String result = translator.translate(countryCode, languageCode);

        if (result == null) {
            result = "no translation found!";
        }

        resultLabel.setText(result);
    }
}
