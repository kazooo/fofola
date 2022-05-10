import {useTranslation} from 'react-i18next';
import {Checkbox} from './Checkbox';

export const LabeledCheckbox = ({label, checked, onChange}) => {
    const {t} = useTranslation();

    return (
        <Checkbox
            label={t(label)}
            checked={checked}
            onChange={onChange}
        />
    );
};
